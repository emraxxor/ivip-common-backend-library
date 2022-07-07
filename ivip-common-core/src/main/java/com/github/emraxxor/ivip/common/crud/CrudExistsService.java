package com.github.emraxxor.ivip.common.crud;


import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.Response;
import com.github.emraxxor.ivip.common.response.Result;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CrudExistsService<
        ID extends Serializable,
        ENTITY extends Result,
        DTO extends Response,
        FILTER extends Filter> extends CrudService<ID, DTO, FILTER> {

    <U extends Response> Boolean exists(U dto);

    <U extends Response> Map<String, Boolean> existsColumnList(U dto, List<String> columns);

    Class<ENTITY> getEntityClass();

}
