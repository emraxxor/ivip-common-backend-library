package com.github.emraxxor.ivip.common.crud;


public interface CrudMapper<ENTITY, DTO> {

    ENTITY mapToEntity(DTO dto);

    ENTITY mapToEntity(DTO dto, ENTITY entity);

    DTO mapToDTO(ENTITY entity);

}
