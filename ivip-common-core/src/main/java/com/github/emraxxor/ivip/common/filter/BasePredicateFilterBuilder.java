package com.github.emraxxor.ivip.common.filter;

import com.github.emraxxor.ivip.common.dto.JoinPath;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A simple utility component for filtering queries.
 * For correct operation, the name of the filter field when generating a query must match the name of the field in the entity.
 *
 * @author Attila Barna
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BasePredicateFilterBuilder implements PredicateFilterBuilder {

    final CriteriaBuilder criteriaBuilder;

    final List<Predicate> predicateList = new ArrayList<>();

    Path<?> path;

    private BasePredicateFilterBuilder(Path<?> path, CriteriaBuilder builder) {
        this.path = path;
        this.criteriaBuilder = builder;
    }

    public static BasePredicateFilterBuilder with(Path<?> path, CriteriaBuilder builder) {
        return new BasePredicateFilterBuilder(path, builder);
    }

    public BasePredicateFilterBuilder andWith(Path<?> otherPath) {
        this.path = otherPath;
        return this;
    }

    public BasePredicateFilterBuilder withConsumer(Consumer<PredicateFilterBuilder> c) {
        c.accept(this);
        return this;
    }

    private <T extends Filter> BasePredicateFilterBuilder buildJoinPaths(T filter) {
        Arrays
                .stream(filter.getClass().getDeclaredFields())
                .filter(e -> e.getAnnotation(JoinPath.class) != null)
                .forEach(f -> {
                    try {
                        String joinPath = f.getAnnotation(JoinPath.class).path();
                        Method m = filter.getClass().getDeclaredMethod("get" + StringUtils.capitalize(f.getName()));
                        Filter childFilter = (Filter) m.invoke(filter);

                        if ( childFilter != null ) {
                            withFilter(childFilter, joinPath);
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException();
                    }
                });
        return this;
    }

    private <T> BasePredicateFilterBuilder addFilter(Field f, T current) {
        if (current instanceof StringFilter) {
            stringFilter(f.getName(), ((StringFilter) current));
        } else if (current instanceof RangeFilter) {
            rangeFilter(f.getName(), (RangeFilter<?>) current);
        } else if (current instanceof InFilter) {
            inFilter(f.getName(), (InFilter<?>) current);
        } else if (current instanceof ComparableFilter) {
            comparableFilter(f.getName(), (ComparableFilter<?>) current);
        } else if (current instanceof SimpleFilter) {
            simpleFilter(f.getName(), (SimpleFilter<?>) current);
        } else if (current instanceof NoFilter) {
            return this;
        } else {
            throw new UnsupportedOperationException();
        }
        return this;
    }

    public <T extends Filter> BasePredicateFilterBuilder withFilter(T filter) {
        return withFilter(filter, null);
    }


    public <T extends Filter> BasePredicateFilterBuilder withFilter(T filter, String path) {
        Field[] fields = filter.getClass().getDeclaredFields();
        final Path<?> oldPath = this.path;
        Optional.ofNullable(path).ifPresent( e -> andWith( ((Root)this.path ).join(path) ) );

        for (Field f : fields) {
            try {
                Method m = filter.getClass().getDeclaredMethod("get" + StringUtils.capitalize(f.getName()));
                Object current = m.invoke(filter);

                if (current == null || f.getAnnotation(JoinPath.class) != null) {
                    continue;
                }

                addFilter(f, current);

            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }

        Optional.ofNullable(path).ifPresent( e -> andWith(oldPath) );
        buildJoinPaths(filter);
        return this;
    }

    private Path resolvePath(String rawPath) {
        String[] pathElements = rawPath.split("\\.");
        Path<?> resolvedPath = path;
        for (String element : pathElements) {
            resolvedPath = resolvedPath.get(element);
        }
        return resolvedPath;
    }


    public  <U extends Serializable> BasePredicateFilterBuilder simpleFilter(String entityFieldName, SimpleFilter<U> filter) {
        if (filter != null && filter.getValue() != null) {
            Path resolvedPath = resolvePath(entityFieldName);

            switch (filter.getType()) {
                case EQUALS:
                    predicateList.add(criteriaBuilder.equal(resolvedPath, filter.getValue()));
                    break;
                case NOT_EQUALS:
                    predicateList.add(criteriaBuilder.notEqual(resolvedPath, filter.getValue()));
                    break;
                case IS_NULL:
                    predicateList.add(criteriaBuilder.isNull(resolvedPath));
                    break;
                case IS_NOT_NULL:
                    predicateList.add(criteriaBuilder.isNotNull(resolvedPath));
                    break;
                case GE:
                    if ( filter instanceof SimpleFilter.NumberFilter) {
                        predicateList.add(criteriaBuilder.ge( resolvedPath , (Number) filter.getValue() ));
                    } else if ( filter instanceof SimpleFilter.LocalDateFilter ) {
                        predicateList.add(criteriaBuilder.greaterThanOrEqualTo(resolvedPath, criteriaBuilder.literal( (LocalDate) filter.getValue()) ));
                    } else if ( filter instanceof SimpleFilter.LocalDateTimeFilter ) {
                        predicateList.add(criteriaBuilder.greaterThanOrEqualTo(resolvedPath, criteriaBuilder.literal( (LocalDateTime) filter.getValue()) ));
                    } else {
                        throw new IllegalStateException("Not supported type (GE)");
                    }
                    break;
                case GT:
                    if ( filter instanceof SimpleFilter.NumberFilter) {
                        predicateList.add(criteriaBuilder.gt( resolvedPath , (Number) filter.getValue() ));
                    } else if ( filter instanceof SimpleFilter.LocalDateFilter ) {
                        predicateList.add(criteriaBuilder.greaterThan(resolvedPath, criteriaBuilder.literal( (LocalDate) filter.getValue()) ));
                    } else if ( filter instanceof SimpleFilter.LocalDateTimeFilter ) {
                        predicateList.add(criteriaBuilder.greaterThan(resolvedPath, criteriaBuilder.literal( (LocalDateTime) filter.getValue()) ));
                    } else {
                        throw new IllegalStateException("Not supported type (GT)");
                    }
                    break;
                case LT:
                    if ( filter instanceof SimpleFilter.NumberFilter) {
                        predicateList.add(criteriaBuilder.lt( resolvedPath , (Number) filter.getValue() ));
                    } else if ( filter instanceof SimpleFilter.LocalDateFilter ) {
                        predicateList.add(criteriaBuilder.lessThan(resolvedPath, criteriaBuilder.literal( (LocalDate) filter.getValue()) ));
                    } else if ( filter instanceof SimpleFilter.LocalDateTimeFilter ) {
                        predicateList.add(criteriaBuilder.lessThan(resolvedPath, criteriaBuilder.literal( (LocalDateTime) filter.getValue()) ));
                    } else {
                        throw new IllegalStateException("Not supported type  (GT)");
                    }
                    break;
                case LE:
                    if ( filter instanceof SimpleFilter.NumberFilter) {
                        predicateList.add(criteriaBuilder.le( resolvedPath , (Number) filter.getValue() ));
                    } else if ( filter instanceof SimpleFilter.LocalDateFilter ) {
                        predicateList.add(criteriaBuilder.lessThanOrEqualTo(resolvedPath, criteriaBuilder.literal( (LocalDate) filter.getValue()) ));
                    } else if ( filter instanceof SimpleFilter.LocalDateTimeFilter ) {
                        predicateList.add(criteriaBuilder.lessThanOrEqualTo(resolvedPath, criteriaBuilder.literal( (LocalDateTime) filter.getValue()) ));
                    } else {
                        throw new IllegalStateException("Not supported type  (GT)");
                    }
                    break;
                default:
                    throwFilterProcessingError(filter.getType().toString());
            }
        }
        return this;
    }

    public BasePredicateFilterBuilder stringFilter(String entityFieldName, StringFilter filter) {
        if (filter != null && filter.getValue() != null) {
            Expression<String> toBeFiltered = criteriaBuilder.lower(resolvePath(entityFieldName));
            String filterValue = filter.getValue().toLowerCase();
            switch (filter.getType()) {
                case CONTAINS:
                    predicateList.add(criteriaBuilder.like(toBeFiltered, "%" + filterValue + "%"));
                    break;
                case STARTS_WITH:
                    predicateList.add(criteriaBuilder.like(toBeFiltered, filterValue + "%"));
                    break;
                case EXACT_MATCH:
                    predicateList.add(criteriaBuilder.equal(toBeFiltered, filterValue));
                    break;
                case IGNORE_CASE_MATCH:
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(toBeFiltered), filterValue.toLowerCase()));
                    break;
                default:
                    throwFilterProcessingError(filter.getType().toString());
            }
        }
        return this;
    }

     public  <C extends Comparable & Serializable> BasePredicateFilterBuilder comparableFilter(String entityFieldName, ComparableFilter<C> filter) {
        Path resolvedPath = resolvePath(entityFieldName);

        if (filter != null && filter.getValue() != null) {
            switch (filter.getType()) {
                case EQUALS:
                    predicateList.add(criteriaBuilder.equal(resolvedPath, filter.getValue()));
                    break;
                case NOT_EQUALS:
                    predicateList.add(criteriaBuilder.notEqual(resolvedPath, filter.getValue()));
                    break;
                case LESS_THAN:
                    predicateList.add(criteriaBuilder.lessThan(resolvedPath, filter.getValue()));
                    break;
                case LESS_THAN_OR_EQUALS:
                    predicateList.add(criteriaBuilder.lessThanOrEqualTo(resolvedPath, filter.getValue()));
                    break;
                case GREATER_THAN:
                    predicateList.add(criteriaBuilder.greaterThan(resolvedPath, filter.getValue()));
                    break;
                case GREATER_THAN_OR_EQUALS:
                    predicateList.add(criteriaBuilder.greaterThanOrEqualTo(resolvedPath, filter.getValue()));
                    break;
                default:
                    throwFilterProcessingError(filter.getType().toString());
            }
        }
        return this;
    }

    public <C extends Comparable & Serializable> BasePredicateFilterBuilder rangeFilter(String entityFieldName, RangeFilter<C> filter) {
        Path resolvedPath = resolvePath(entityFieldName);

        if (filter == null) {
            return this;
        }

        if (filter.getMinValue() != null && filter.getMaxValue() != null) {
            predicateList.add(criteriaBuilder.between(resolvedPath, filter.getMinValue(), filter.getMaxValue()));
            return this;
        }

        if (filter.getMinValue() != null && filter.getMaxValue() == null) {
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(resolvedPath, filter.getMinValue()));
            return this;
        }

        if (filter.getMinValue() == null && filter.getMaxValue() != null) {
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(resolvedPath, filter.getMaxValue()));
            return this;
        }

        return this;
    }

    public <U extends Serializable> BasePredicateFilterBuilder inFilter(String entityFieldName, InFilter<U> filter) {
        if (filter != null && filter.getValue() != null && !filter.getValue().isEmpty()) {
                CriteriaBuilder.In<U> inPredicate = criteriaBuilder.in(resolvePath(entityFieldName));
                filter.getValue().forEach(inPredicate::value);
                predicateList.add(inPredicate);
        }
        return this;
    }

    public Predicate build() {
        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }


    private void throwFilterProcessingError(String filterTypeName) {
        throw new IllegalArgumentException("Unable to process filter with type: " + filterTypeName);
    }
}
