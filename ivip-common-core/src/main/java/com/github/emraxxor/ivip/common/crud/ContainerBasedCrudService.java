package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.ESExtendedDataElement;
import com.github.emraxxor.ivip.common.es.ESOperationType;
import com.github.emraxxor.ivip.common.es.ScrollSourceData;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.scroll.DefaultScrollResponse;

import java.io.Serializable;

public interface ContainerBasedCrudService<
        ID extends Serializable,
        DTO extends Response,
        RESPONSE_DTO extends Response,
        FILTER extends Filter,
        SCROLL extends ScrollSourceData> {

    <F extends AbstractBaseDataElement> ESExtendedDataElement<F> dataElement(F dto, ESOperationType type);

    DTO create(DTO dto);

    DefaultScrollResponse<RESPONSE_DTO> findAll(FILTER filter, SCROLL scroll);

    DTO findOne(ID id);

    void delete(ID id);

    DTO update(DTO dto, ID id);
}
