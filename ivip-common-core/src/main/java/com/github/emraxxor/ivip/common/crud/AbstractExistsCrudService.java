package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.dto.AnnotatedColumnMetaData;
import com.github.emraxxor.ivip.common.dto.AnnotatedColumnParameter;
import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import com.github.emraxxor.ivip.common.exception.ValidationException;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.ColumnExistsDTO;
import com.github.emraxxor.ivip.common.response.PageResponseMapper;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * AbstractExistsCrudService helps to determine the uniqueness of the specified fields.
 * The component is capable of storing unique entities by checking the existence of a particular field in the database.
 *
 * @param <ID>
 * @param <ENTITY>
 * @param <REPOSITORY>
 * @param <DTO>
 * @param <FILTER>
 *
 * @collection ivip.cloud
 * @author Attila Barna
 * @version 1.0.0
 */
@Slf4j
public abstract class AbstractExistsCrudService<
        DTO extends Response,
        ID extends Serializable,
        FILTER extends Filter,
        ENTITY extends Result & BaseEntityIF<ID>,
        REPOSITORY extends CrudRepository<ENTITY,ID> & JpaSpecificationExecutor<ENTITY> & JpaRepository<ENTITY, ID>
        > extends AbstractCrudService<DTO,ID,FILTER,ENTITY, REPOSITORY>
            implements CrudExistsService<ID, ENTITY, DTO, FILTER>  {

    protected AbstractExistsCrudService(REPOSITORY repository, CrudMapper<ENTITY, DTO> mapper, PageResponseMapper<ENTITY, DTO> pageMapper) {
        super(repository, mapper, pageMapper);
    }

    @Getter
    @Setter
    @Builder(builderMethodName = "column")
    public static class ColumnMetaData {
        String columnName;
        List<Class<?>> parameterTypes;
        List<Object> args;
    }


    private AnnotatedColumnMetaData toColumnMetaData(@NotNull Field field) {
        return field.getAnnotation(AnnotatedColumnMetaData.class);
    }

    private List<Class<?>> toParameterTypes(@NotNull Field field) {
        return Arrays
                    .stream(toColumnMetaData(field).parameters())
                    .map(AnnotatedColumnParameter::parameterType)
                    .collect(Collectors.toList());
    }

    private <U extends Response> List<Object> toParameterArgs(@NotNull Field field, U response) {
        try {
            Method m = response.getClass().getMethod("get" + capitalize(field.getName()));
            Object value = m.invoke(response);
            return Lists.newArrayList(value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            throw new ValidationException(e);
        }
    }

    @Transactional(rollbackFor = ValidationException.class)
    public <U extends Response> Boolean exists(U response) {
        Field[] fields = response.getClass().getDeclaredFields();
        try {
            return exists(
              null,
              Arrays.stream(fields)
                            .filter(e -> e.getAnnotation(AnnotatedColumnMetaData.class) != null)
                            .collect(Collectors.toMap(Field::getName, e -> ColumnMetaData
                                    .column()
                                    .parameterTypes( toParameterTypes(e) )
                                    .args( toParameterArgs(e, response) )
                                    .build()
                                    )
              )  , getEntityClass() );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ValidationException(e);
        }
    }

    private Field findFieldByColumnName(String columName, Response dto) {
        try {
            return dto.getClass().getDeclaredField(columName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = ValidationException.class)
    public <U extends Response> Map<String, Boolean> existsColumnList(U dto, List<String> columns) {
        return columns
                                  .stream()
                                  .map(e -> findFieldByColumnName(e,dto) )
                                  .filter(Objects::nonNull)
                                  .collect(Collectors.toMap(Field::getName, x -> {
                                        try {
                                            return exists( dto,
                                                    Map.of( x.getName(), ColumnMetaData
                                                                            .column()
                                                                            .parameterTypes(toParameterTypes(x))
                                                                            .args(toParameterArgs(x, dto))
                                                                            .build()
                                                    ), getEntityClass()
                                            );
                                        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                                            log.error(e.getMessage());
                                        }
                                        return false;
                                  }));
    }

    @Transactional(propagation = Propagation.MANDATORY, rollbackFor = ValidationException.class)
    public  <U extends Response> Boolean exists(U dto, Map<String, ColumnMetaData> columns, Class<ENTITY> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        try {
            final ENTITY entity = clazz.getConstructor().newInstance();
            final ExampleMatcher matcher = ExampleMatcher.matching();

            columns
                    .forEach((key, value) -> {
                        try {
                            Method m = entity
                                    .getClass()
                                    .getMethod("set" + capitalize(key), value.getParameterTypes().toArray(Class[]::new));
                            m.invoke(entity, value.getArgs().toArray());
                        } catch (NoSuchMethodException ex) {
                            log.error(ex.getMessage());
                        } catch (InvocationTargetException | IllegalAccessException invocationTargetException) {
                            invocationTargetException.printStackTrace();
                        }
                    });


            columns.forEach((key, value) -> matcher.withMatcher(key, ExampleMatcher.GenericPropertyMatchers.ignoreCase()));

            Example<ENTITY> example = Example.of(entity, matcher);

            return Optional.ofNullable(dto)
                    .filter(e -> e instanceof ColumnExistsDTO && entity instanceof ExistsEntity )
                    .map( e ->
                                    this
                                            .repository
                                            .findAll(example)
                                            .stream()
                                            .anyMatch(match -> ! ((ExistsEntity<?>)match).id() .equals( ((ColumnExistsDTO<?>)dto).id())))
                    .orElseGet(() -> this.repository.exists(example));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error(e.getMessage());
            throw new ValidationException(e);
        }
    }
}
