package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.OK;

/**
 * "AbstractCrudResponseController" is also a useful component for managing "CRUD" operations.
 * "AbstractCrudResponseController" is extended from "AbstractCrudController", the difference
 *  between them is the type of the response. In this type, the type of the response can be specified.
 *
 * This component supports the handling of the "DTO" required for a response.
 *
 * @param <ID>
 * @param <ENTITY>
 * @param <DTO>
 * @param <FILTER>
 * @param <RESPONSE_DTO>
 * @param <RESPONSE_FILTER>
 *
 * @author Attila Barna
 */
public abstract class AbstractCrudResponseController<
        ID extends Serializable,
        ENTITY extends Result & BaseEntityIF<ID>,
        DTO extends Response,
        FILTER extends Filter,
        RESPONSE_DTO extends Response,
        RESPONSE_FILTER extends Filter
        > extends AbstractCrudController<ID, ENTITY, DTO, FILTER> implements
        CrudResponseController<ID, RESPONSE_DTO, RESPONSE_FILTER> {


    protected AbstractCrudResponseController(CrudResponseService<ID, DTO, FILTER, RESPONSE_DTO, RESPONSE_FILTER> service) {
        super(service);
    }

    @Override
    public ResponseEntity<RESPONSE_DTO> findOneResponseType(ID id) {
        return new ResponseEntity<>( ((CrudResponseService<ID, DTO, FILTER, RESPONSE_DTO, RESPONSE_FILTER>)service).findOneResponseType(id), OK);
    }

    @Override
    public ResponseEntity<PageResponse<RESPONSE_DTO>> findAll(RESPONSE_FILTER filter, Pageable pageable) {
        return new ResponseEntity<>( ((CrudResponseService<ID, DTO, FILTER, RESPONSE_DTO, RESPONSE_FILTER>)service).findAll(filter, pageable), OK);
    }
}
