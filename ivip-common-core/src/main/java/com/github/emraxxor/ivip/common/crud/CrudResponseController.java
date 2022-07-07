package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;

public interface CrudResponseController<
        ID extends Serializable,
        RESPONSE_DTO extends Response,
        RESPONSE_FILTER extends Filter
        >
{

    @GetMapping("/type/response/id/{id}")
    ResponseEntity<RESPONSE_DTO> findOneResponseType(ID id);

    @GetMapping("/findAll")
    ResponseEntity<PageResponse<RESPONSE_DTO>> findAll(RESPONSE_FILTER filter, Pageable pageable);
}
