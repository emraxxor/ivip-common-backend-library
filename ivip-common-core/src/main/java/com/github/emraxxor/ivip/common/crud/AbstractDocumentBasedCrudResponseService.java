package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.filter.PredicateFilterBuilder;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.PageResponseMapper;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 *  Service for creating CRUD operations.
 *  The difference compared to a simple service is that a different type of response can be specified in this service.
 *
 *  Notice:
 *  By default, the document-based service does not support handling filters
 *  because they can be too varied from service to service.
 *
 *  By default, these methods (#findAll,#listAll) should be implemented on a per-service basis.
 *
 * @param <ID>
 * @param <ENTITY>
 * @param <REPOSITORY>
 * @param <DTO>
 * @param <RESPONSE_DTO>
 * @param <FILTER>
 * @param <RESPONSE_FILTER>
 *
 * @author Attila Barna
 */
public abstract class AbstractDocumentBasedCrudResponseService<
        ID extends Serializable,
        ENTITY extends Result & DocumentEntity,
        REPOSITORY extends CrudRepository<ENTITY,ID> & DocumentRepository<ENTITY>,
        DTO extends  Response,
        FILTER extends Filter,
        RESPONSE_DTO extends Response,
        RESPONSE_FILTER extends Filter
        > extends AbstractDocumentBasedCrudService<ID, ENTITY, REPOSITORY, DTO, FILTER> implements
        CrudResponseService<ID, DTO, FILTER, RESPONSE_DTO, RESPONSE_FILTER>, DocumentService {


    protected final CrudMapper<ENTITY, RESPONSE_DTO> responseMapper;

    protected final PageResponseMapper<ENTITY,RESPONSE_DTO> responsePageMapper;

    protected AbstractDocumentBasedCrudResponseService(
            REPOSITORY repository,
            CrudMapper<ENTITY, DTO> mapper,
            PageResponseMapper<ENTITY, DTO> pageMapper,
            CrudMapper<ENTITY, RESPONSE_DTO> responseMapper,
            PageResponseMapper<ENTITY, RESPONSE_DTO> responsePageMapper
    ) {
        super(repository, mapper, pageMapper);
        this.responseMapper = responseMapper;
        this.responsePageMapper = responsePageMapper;
    }


    @Override
    public RESPONSE_DTO findOneResponseType(ID id) {
        return responsePageMapper.map(getEntityById(id));
    }

    @Override
    public PageResponse<RESPONSE_DTO> findAll(RESPONSE_FILTER filter, Pageable pageable) {
        return responsePageMapper.mapPage(repository.findAll().iterator() );
    }

    @Override
    public PageResponse<RESPONSE_DTO> findAll(Consumer<PredicateFilterBuilder> consumer, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PageResponse<DTO> listAll(FILTER filter, Pageable pageable) {
        return pageMapper.mapPage(repository.findAll().iterator() );
    }

    @Override
    public PageResponse<DTO> listAll(Consumer<PredicateFilterBuilder> consumer, Pageable pageable) {
        throw new UnsupportedOperationException();
    }

}
