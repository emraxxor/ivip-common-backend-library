package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.filter.PredicateFilterBuilder;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.function.Consumer;

public interface CrudResponseService<
        ID extends Serializable,
        DTO extends Response,
        FILTER extends Filter,
        RESPONSE_DTO extends Response,
        RESPONSE_FILTER extends Filter
        > extends CrudService<ID, DTO, FILTER> {

    RESPONSE_DTO findOneResponseType(ID id);

    PageResponse<RESPONSE_DTO> findAll(RESPONSE_FILTER filter, Pageable pageable);

    PageResponse<RESPONSE_DTO> findAll(Consumer<PredicateFilterBuilder> consumer, Pageable pageable);
}
