package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.dto.AnnotatedColumnMetaData;
import com.github.emraxxor.ivip.common.dto.DTOUniqueColumn;
import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 * This controller is suitable for checking the unique existence of fields.
 * The main idea behind creating this component is to check the existence of the record before it is created.
 * The uniqueness of the record is examined in a suitable "service" {@link CrudExistsService}.
 *
 * @param <ID>
 * @param <ENTITY>
 * @param <DTO>
 * @param <FILTER>
 *
 * @author Attila Barna
 */
public abstract class AbstractExistsCrudController<
        ID extends Serializable,
        ENTITY extends Result & BaseEntityIF<ID>,
        DTO extends Response,
        FILTER extends Filter
        > extends AbstractCrudController<ID, ENTITY, DTO, FILTER>
          implements CrudExistsController<ID, DTO, FILTER> {

    CrudExistsService<ID, ENTITY, DTO, FILTER> service;

    protected AbstractExistsCrudController(CrudExistsService<ID, ENTITY, DTO, FILTER> service) {
        super(service);
        this.service = service;
    }

    @Override
    public ResponseEntity<Boolean> checkUniqueRow(DTO dto) {
        return ResponseEntity.ok(service.exists(dto));
    }

    @Override
    public <T> ResponseEntity<Map<String, Boolean>> checkUniqueColumns(T column) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> checkUniqueColumns(@RequestBody DTOUniqueColumn<DTO> dtoUniqueColumn) {
        return ResponseEntity.ok(service.existsColumnList(dtoUniqueColumn.dto(), dtoUniqueColumn.columns()));
    }

    @Override
    public ResponseEntity<DTO> update(@Valid @RequestBody DTO dto, @PathVariable ID id) {
        if (Boolean.TRUE.equals(existsColumnList(dto))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return super.update(dto, id);
    }

    private Boolean existsColumnList(DTO dto) {
        return service
                .existsColumnList(dto, uniqueColumnList(dto)).values()
                .stream()
                .filter(e -> e).findAny()
                .orElse(false);
    }

    @Override
    public ResponseEntity<DTO> create(@Valid @RequestBody DTO dto) {
        if (Boolean.TRUE.equals(existsColumnList(dto))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return super.create(dto);
    }

    @Override
    public List<String> uniqueColumnList(DTO dto) {
        return Arrays
                .stream(dto.getClass().getDeclaredFields())
                .filter(e -> e.getAnnotation(AnnotatedColumnMetaData.class) != null)
                .map(Field::getName)
                .collect(Collectors.toList());
    }
}
