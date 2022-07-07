package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface CrudResponseClientController<
        ID,
        RESPONSE_DTO extends Response,
        FILTER extends Filter
        > {

    @GetMapping("/type/response/id/{id}")
    ResponseEntity<RESPONSE_DTO> findOneResponseType(@PathVariable ID id);

    @GetMapping("/type/response/findAll")
    ResponseEntity<PageResponse<RESPONSE_DTO>> findAll(@SpringQueryMap FILTER filter, @SpringQueryMap Pageable pageable);
}
