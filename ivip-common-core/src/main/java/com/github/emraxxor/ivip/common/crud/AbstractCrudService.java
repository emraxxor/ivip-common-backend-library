package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import com.github.emraxxor.ivip.common.filter.BasePredicateFilterBuilder;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.filter.PredicateFilterBuilder;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.PageResponseMapper;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Simple CRUD service for JPA repositories.
 * Service implementation for {@link AbstractCrudController}
 *
 * @param <ID>
 * @param <ENTITY>
 * @param <REPOSITORY>
 * @author Attila Barna
 */
public abstract class AbstractCrudService<
            DTO extends Response,
            ID extends Serializable,
            FILTER extends Filter,
            ENTITY extends Result & BaseEntityIF<ID>,
            REPOSITORY extends CrudRepository<ENTITY,ID> & JpaSpecificationExecutor<ENTITY> & JpaRepository<ENTITY, ID>
    > implements CrudService<ID, DTO, FILTER>, EntityService<ID,ENTITY> {

    protected final REPOSITORY repository;

    protected final CrudMapper<ENTITY, DTO> mapper;

    protected final PageResponseMapper<ENTITY, DTO> pageMapper;

    protected AbstractCrudService(REPOSITORY repository, CrudMapper<ENTITY, DTO> mapper, PageResponseMapper<ENTITY, DTO> pageMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.pageMapper = pageMapper;
    }

    @Override
    public  DTO create(DTO dto) {
        return mapper.mapToDTO( repository.save(mapper.mapToEntity(dto)) );
    }

    @Override
    public DTO findOne(ID id) {
        return mapper.mapToDTO(getEntityById(id));
    }

    @Override
    public PageResponse<DTO> listAll(FILTER filter, Pageable pageable) {
        return pageMapper.mapPage(
                repository.findAll(
                        (root, query, builder) -> BasePredicateFilterBuilder.with(root, builder)
                                .withFilter(filter)
                                .build(), pageable)
        );
    }

    @Override
    public PageResponse<DTO> listAll(Consumer<PredicateFilterBuilder> consumer, Pageable pageable) {
        return pageMapper.mapPage(
                repository.findAll(
                        (root, query, builder) -> BasePredicateFilterBuilder.with(root, builder)
                                .withConsumer(consumer)
                                .build(), pageable)
        );
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

}
