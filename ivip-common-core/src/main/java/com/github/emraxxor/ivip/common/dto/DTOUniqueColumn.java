package com.github.emraxxor.ivip.common.dto;

import com.github.emraxxor.ivip.common.response.Response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DTOUniqueColumn<DTO extends Response> implements UniqueColumn<DTO, String> {

    DTO dto;

    List<String> columns;

    @Override
    public List<String> columns() {
        return this.columns;
    }

    @Override
    public DTO dto() {
        return this.dto;
    }
}
