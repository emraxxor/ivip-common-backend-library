package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.dto.DTOUniqueColumn;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CrudExistsController<
        ID extends Serializable,
        DTO extends Response,
        FILTER extends Filter
        >
    extends CrudController<ID, DTO, FILTER>
{

    @GetMapping("/check/unique/row")
    ResponseEntity<Boolean> checkUniqueRow(DTO dto);

    @GetMapping("/check/unique/columns")
    <T> ResponseEntity<Map<String,Boolean>> checkUniqueColumns(T column);

    @PostMapping("/check/unique/columnList")
    ResponseEntity<Map<String, Boolean>> checkUniqueColumns(@RequestBody DTOUniqueColumn<DTO> dtoUniqueColumn);

    List<String> uniqueColumnList(DTO dto);

}
