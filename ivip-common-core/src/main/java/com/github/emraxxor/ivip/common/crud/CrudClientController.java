package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Interface for openfeign clients
 *
 * @param <ID>
 * @param <DTO>
 * @param <FILTER>
 *
 * @author Attila Barna
 */
public interface CrudClientController<ID, DTO extends Response, FILTER extends Filter> {

    @PostMapping
    ResponseEntity<DTO> create(@Valid DTO dto);

    @GetMapping
    ResponseEntity<PageResponse<DTO>> listAll(@SpringQueryMap FILTER filter, @SpringQueryMap Pageable pageable);

    @GetMapping("/id/{id}")
    ResponseEntity<DTO> findOne(@PathVariable ID id);

    @PutMapping("/{id}")
    ResponseEntity<DTO> update(@Valid DTO dto, @PathVariable ID id);

    @DeleteMapping("/id/{id}")
    ResponseEntity<Void> delete(@PathVariable ID id);

}
