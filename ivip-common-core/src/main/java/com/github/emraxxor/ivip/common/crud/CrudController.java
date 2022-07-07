package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.Serializable;

public interface CrudController<ID extends Serializable, DTO extends Response, FILTER extends Filter> {

    @PostMapping
    ResponseEntity<DTO> create(DTO dto);

    @GetMapping
    ResponseEntity<PageResponse<DTO>> listAll(FILTER filter, Pageable pageable);

    @GetMapping("/id/{id}")
    ResponseEntity<DTO> findOne(ID id);

    @PutMapping("/{id}")
    ResponseEntity<DTO> update(DTO dto, ID id);

    @DeleteMapping("/id/{id}")
    ResponseEntity<Void> delete(ID id);
}
