package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.filter.PredicateFilterBuilder;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.PageResponseMapper;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 *
 *  Service for creating CRUD operations.
 *  The difference compared to a simple service is that a different type of response can not be specified in this service.
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
 * @author Attila Barna
 */
@RequiredArgsConstructor
public abstract class AbstractDocumentBasedCrudService<
        ID extends Serializable,
        ENTITY extends Result & DocumentEntity,
        REPOSITORY extends CrudRepository<ENTITY,ID> & DocumentRepository<ENTITY>,
        DTO extends  Response,
        FILTER extends Filter
        > implements CrudService<ID, DTO, FILTER>, DocumentService, EntityService<ID, ENTITY> {

    protected final REPOSITORY repository;

    protected final CrudMapper<ENTITY, DTO> mapper;

    protected final PageResponseMapper<ENTITY, DTO> pageMapper;

    @Override
    public DTO create(DTO dto) {
        return mapper.mapToDTO( repository.save(mapper.mapToEntity(dto)) );
    }

    @Override
    public DTO findOne(ID id) {
        return mapper.mapToDTO(getEntityById(id));
    }

    @Override
    public ENTITY getEntityById(ID id) {
        return repository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Override
    public void delete(ID id) {
        repository.delete(getEntityById(id));
    }

    @Override
    public DTO update(DTO dto, ID id) {
        return mapper.mapToDTO(repository.save(mapper.mapToEntity(dto, getEntityById(id))));
    }

    @Override
    public PageResponse<DTO> listAll(FILTER filter, Pageable pageable) {
        return pageMapper.mapPage( new PageImpl<>( IteratorUtils.toList(repository.findAll().iterator()) ) );
    }

    @Override
    public PageResponse<DTO> listAll(Consumer<PredicateFilterBuilder> consumer, Pageable pageable) {
        return pageMapper.mapPage( new PageImpl<>( IteratorUtils.toList(repository.findAll().iterator()) ) );
    }
}
