package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.es.ScrollSourceData;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.scroll.DefaultScrollResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;

public interface ContainerBasedCrudController<
        ID extends Serializable,
        DTO extends Response,
        RESPONSE_DTO extends Response,
        FILTER extends Filter,
        SCROLL extends ScrollSourceData>  {

    @PostMapping
    ResponseEntity<DTO> create(@Valid @RequestBody DTO dto);

    @GetMapping
    ResponseEntity<DefaultScrollResponse<RESPONSE_DTO>> findAll(@Valid FILTER filter, @Valid SCROLL scroll);

    @GetMapping("/id/{id}")
    ResponseEntity<DTO> findOne(@PathVariable  ID id);

    @PutMapping("/{id}")
    ResponseEntity<DTO> update(@Valid @RequestBody DTO dto, @PathVariable ID id);

    @DeleteMapping("/id/{id}")
    ResponseEntity<Void> delete(@PathVariable  ID id);

}
