package com.github.emraxxor.ivip.common.crud;


import com.github.emraxxor.ivip.common.es.*;
import com.github.emraxxor.ivip.common.es.container.ESContainerIF;
import com.github.emraxxor.ivip.common.es.manager.DocumentManager;
import com.github.emraxxor.ivip.common.filter.DocumentBoolQueryBuilder;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.scroll.AbstractScrollResponse;
import com.github.emraxxor.ivip.common.scroll.DefaultBoolScrollSourceResponse;
import com.github.emraxxor.ivip.common.scroll.DefaultScrollResponse;
import com.github.emraxxor.ivip.common.scroll.ScrollResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.Serializable;

/**
 *
 * Implements high-level abstractions required for "CRUD" service.
 *
 * This service is suitable for managing document-based elements, for more information see {@link com.github.emraxxor.ivip.common.es.container.ESContainer}
 *
 *
 * @param <ID>
 * @param <DTO>
 * @param <RESPONSE_DTO>
 * @param <FILTER>
 * @param <SCROLL>
 */
@RequiredArgsConstructor
public abstract class AbstractContainerBasedCrudService<
        ID extends Serializable,
        DTO extends AbstractBaseDataElement,
        RESPONSE_DTO extends AbstractBaseDataElement,
        FILTER extends Filter,
        SCROLL extends ScrollSourceData,
        MAPPER extends DocumentMapper
        > implements ContainerBasedCrudService<ID,DTO,RESPONSE_DTO,FILTER,SCROLL> {

    protected final DocumentManager documentManager;

    protected final ESContainerIF<ESExtendedDataElement<?>> esContainer;

    protected final ScrollResponseGenerator scrollResponseGenerator;

    protected final MAPPER mapper;

    @Override
    public DTO create(DTO dto) {
        return (DTO) createItem(mapper.mapToCreate(dto));
    }

    public final <T extends AbstractBaseDataElement> T createItem(T e) {
        return esContainer.createSynchronous(dataElement(e, ESOperationType.INDEX));
    }

    public final <T extends AbstractBaseDataElement> T updateItem(T e) {
        return esContainer.createSynchronous(dataElement(e, ESOperationType.UPDATE));
    }

    public final <T extends AbstractBaseDataElement> T deleteItem(T e) {
        return esContainer.createSynchronous(dataElement(e, ESOperationType.DELETE));
    }

    @Override
    public DefaultScrollResponse<RESPONSE_DTO> findAll(FILTER filter, SCROLL scroll) {
        var scrollSource = scrollResponseSource(scroll);
        DocumentBoolQueryBuilder.builder().query(QueryBuilders.boolQuery()).scroll(scrollSource).build().with(filter);
        return scrollResponseGenerator.generate(scrollResponse(), scrollSource);
    }

    protected abstract DefaultBoolScrollSourceResponse<RESPONSE_DTO, DefaultScrollSourceData> scrollResponseSource(SCROLL scroll);

    protected DefaultScrollResponse<RESPONSE_DTO> scrollResponse() {
        return new DefaultScrollResponse<>();
    }

    @Override
    public void delete(ID id) {
       deleteItem(mapper.mapToDelete(findOne(id)));
    }

    @Override
    public DTO update(DTO dto, ID id) {
        return (DTO)updateItem(mapper.mapToUpdate(dto, findOne(id)));
    }
    
}