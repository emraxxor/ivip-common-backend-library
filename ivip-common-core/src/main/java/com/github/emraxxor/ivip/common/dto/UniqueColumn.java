package com.github.emraxxor.ivip.common.dto;


import com.github.emraxxor.ivip.common.response.Response;

import java.io.Serializable;
import java.util.List;

public interface UniqueColumn<
        DTO extends Response,
        E extends Serializable
        > {

    List<E> columns();

    DTO dto();
}
