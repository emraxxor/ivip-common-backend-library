package com.github.emraxxor.ivip.common.handler;

import com.github.emraxxor.ivip.common.exception.BusinessValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Default configuration for exception handlers
 *
 * @author Attila Barna
 */
@Primary
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        BusinessValidationError error = new BusinessValidationError(
                Instant.now(),
                1,
                ex.getMessage(),
                request.getDescription(false),
                List.of(new BusinessValidationErrorDetail(ex.getMessage(), null, null)));
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = { AssertionError.class})
    protected ResponseEntity<Object> handleAssertionError(AssertionError ex, WebRequest request) {
        BusinessValidationError error = new BusinessValidationError(
                Instant.now(),
                1,
                ex.getMessage(),
                request.getDescription(false),
                List.of(new BusinessValidationErrorDetail(ex.getMessage(), null, null)));
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public final ResponseEntity<BusinessValidationError> handleBusinessValidationException(BusinessValidationException e, WebRequest request) {
        BusinessValidationError error = new BusinessValidationError(
                Instant.now(),
                e.getStatusCode(),
                e.getStatusMessage(),
                request.getDescription(false),
                List.of(new BusinessValidationErrorDetail(e.getMessage(), e.getField(), e.getRejectedValue())) );
        return new ResponseEntity<>(error, e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<BusinessValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        BusinessValidationError error = mapBindingResult(ex.getBindingResult(), request);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResult(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        log.error(ex.getMessage(), ex);

        if (ex instanceof NullPointerException)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BusinessValidationError> handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
        return new ResponseEntity<>(
                new BusinessValidationError(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.toString(),
                        request.getDescription(false),
                        ex.getConstraintViolations().stream()
                                .map(e -> {
                                    if (e instanceof FieldError) {
                                        return mapFieldError((FieldError) e);
                                    }
                                    return mapConstraintViolation(e);
                                }).
                                collect(Collectors.toList())), HttpStatus.BAD_REQUEST);
    }

    private BusinessValidationErrorDetail mapConstraintViolation(ConstraintViolation constraintViolation) {

        ConstraintDescriptor<?> constraintDescriptor = constraintViolation.getConstraintDescriptor();

        return new BusinessValidationErrorDetail(
                constraintViolation.getMessage(),
                Optional.ofNullable(constraintViolation.getPropertyPath())
                        .map(Path::toString)
                        .orElse(null),
                Optional.ofNullable(constraintViolation.getInvalidValue())
                        .map(String::valueOf)
                        .orElse(null),
                constraintDescriptor.getAttributes().entrySet().stream()
                        .filter(e -> !"message".equals(e.getKey()))
                        .filter(e -> BeanUtils.isSimpleValueType(e.getValue().getClass()))
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())))
        );
    }

    private BusinessValidationError mapBindingResult(BindingResult bindingResult, WebRequest request) {
        return new BusinessValidationError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                request.getDescription(false),
                bindingResult.getAllErrors().stream()
                        .map(e -> {
                            if (e instanceof FieldError) {
                                return mapFieldError((FieldError) e);
                            } else {
                                return mapObjectError(e);
                            }
                        }).
                        collect(Collectors.toList()));
    }

    public final BusinessValidationErrorDetail mapObjectError(ObjectError error) {
        return new BusinessValidationErrorDetail(error.getDefaultMessage(), null, null);
    }


    public final BusinessValidationErrorDetail mapFieldError(FieldError error) {
        if (!error.contains(ConstraintViolation.class))
            return new BusinessValidationErrorDetail(error.getDefaultMessage(), error.getField(), error.getRejectedValue() == null ? null : String.valueOf(error.getRejectedValue()));

        ConstraintDescriptor<?> constraintDescriptor = error.unwrap(ConstraintViolation.class).getConstraintDescriptor();

        return new BusinessValidationErrorDetail(
                error.getDefaultMessage(),
                error.getField(),
                error.getRejectedValue() == null ? null : String.valueOf(error.getRejectedValue()),
                constraintDescriptor.getAttributes().entrySet().stream()
                        .filter(e -> !"message".equals(e.getKey()))
                        .filter(e -> BeanUtils.isSimpleValueType(e.getValue().getClass()))
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())))
        );
    }
}
