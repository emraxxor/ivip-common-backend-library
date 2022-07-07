package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import com.github.emraxxor.ivip.common.filter.BasePredicateFilterBuilder;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.filter.PredicateFilterBuilder;
import com.github.emraxxor.ivip.common.response.PageResponse;
import com.github.emraxxor.ivip.common.response.PageResponseMapper;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.function.Consumer;


/**
 * Simple CRUD service for JPA repositories.
 *
 * Service implementation for {@link AbstractCrudResponseController}
 *
 * @param <ID>
 * @param <ENTITY>
 * @param <DTO>
 * @param <RESPONSE_DTO>
 * @param <FILTER>
 * @param <RESPONSE_FILTER>
 * @param <REPOSITORY>
 * @author Attila Barna
 */
public abstract class AbstractCrudResponseService<
        ID extends Serializable,
        ENTITY extends Result & BaseEntityIF<ID>,
        DTO extends Response,
        RESPONSE_DTO extends Response,
        FILTER extends Filter,
        RESPONSE_FILTER extends Filter,
        REPOSITORY extends CrudRepository<ENTITY,ID> & JpaSpecificationExecutor<ENTITY> & JpaRepository<ENTITY, ID>
        > extends AbstractCrudService<DTO, ID, FILTER, ENTITY, REPOSITORY>
        implements CrudResponseService<ID, DTO, FILTER, RESPONSE_DTO, RESPONSE_FILTER>

{
    protected final CrudMapper<ENTITY, RESPONSE_DTO> responseMapper;

    protected final PageResponseMapper<ENTITY, RESPONSE_DTO> responsePageMapper;

    protected AbstractCrudResponseService(
                REPOSITORY repository,
                CrudMapper<ENTITY, DTO > mapper,
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
        return responsePageMapper.mapPage(
                repository.findAll((root, query, criteriaBuilder) -> BasePredicateFilterBuilder.with(root,criteriaBuilder)
                        .withFilter(filter)
                        .build(), pageable)
        );
    }

    @Override
    public PageResponse<RESPONSE_DTO> findAll(Consumer<PredicateFilterBuilder> consumer, Pageable pageable) {
        return responsePageMapper.mapPage(
                repository.findAll((root, query, criteriaBuilder) -> BasePredicateFilterBuilder.with(root,criteriaBuilder)
                        .withConsumer(consumer)
                        .build(), pageable)
        );
    }

}
