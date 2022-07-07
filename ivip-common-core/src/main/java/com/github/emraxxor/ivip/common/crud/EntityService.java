package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.response.Result;

import java.io.Serializable;

public interface EntityService<ID extends Serializable, ENTITY extends Result> {
    ENTITY getEntityById(ID id);
}
