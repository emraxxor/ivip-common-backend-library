package com.github.emraxxor.ivip.common.response;

import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Iterator;
import java.util.stream.Collectors;

public interface PageResponseMapper <RESULT extends Result, RESPONSE extends Response> {

    RESPONSE map(RESULT result);

    default PageResponse<RESPONSE> mapPage(Page<RESULT> page) {
        return new PageResponse<>(
                page
                    .getContent()
                        .stream()
                        .map(this::map)
                        .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );
    }

    default PageResponse<RESPONSE> mapPage(Iterator<RESULT> data) {
        var page = new PageImpl<>(IteratorUtils.toList(data));
        return new PageResponse<>(
                page
                        .getContent()
                        .stream()
                        .map(this::map)
                        .collect(Collectors.toList()),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );
    }


}
