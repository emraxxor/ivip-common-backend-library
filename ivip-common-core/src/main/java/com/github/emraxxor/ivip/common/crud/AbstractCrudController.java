package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.Serializable;

import static org.springframework.http.HttpStatus.*;

/**
 * Simple "high-level" abstraction for crud operations.
 *
 * In AbstractCrudController, the type required for the response is the same as the type required for creation,
 * if you want to use a different type for a response then use {@link AbstractCrudResponseController}
 *
 * @param <ID>
 * @param <ENTITY>
 * @param <DTO>
 * @param <FILTER>
 *
 * @author Attila Barna
 */
public abstract class AbstractCrudController<
        ID extends Serializable,
        ENTITY extends Result & BaseEntityIF<ID>,
        DTO extends Response,
        FILTER extends Filter
        > implements CrudController<ID, DTO, FILTER> {

    protected CrudService<ID, DTO, FILTER> service;

    protected AbstractCrudController(CrudService<ID, DTO, FILTER> service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<DTO> create(@Valid @RequestBody DTO dto) {
        return new ResponseEntity<>(service.create(dto), CREATED);
    }

    @Override
    public ResponseEntity<PageResponse<DTO>> listAll(FILTER filter, Pageable pageable) {
        return new ResponseEntity<>(service.listAll(filter, pageable), OK);
    }

    @Override
    public ResponseEntity<DTO> findOne(@PathVariable ID id) {
        return new ResponseEntity<>(service.findOne(id), OK);
    }

    @Override
    public ResponseEntity<DTO> update(@Valid @RequestBody DTO dto, @PathVariable ID id) {
        return new ResponseEntity<>(service.update(dto, id), OK);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
