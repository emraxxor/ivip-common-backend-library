package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.filter.PredicateFilterBuilder;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Simple service interface that helps to manage crud operations
 *
 * @param <DTO>
 * @param <ID>
 */
public interface CrudService<
        ID extends Serializable,
        DTO extends Response,
        FILTER extends Filter
    > {

    DTO create(DTO dto);

    PageResponse<DTO> listAll(FILTER filter, Pageable pageable);

    PageResponse<DTO> listAll(Consumer<PredicateFilterBuilder> consumer, Pageable pageable);

    DTO findOne(ID id);

    void delete(ID id);

    DTO update(DTO dto, ID id);
}
