package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.ScrollSourceData;
import com.github.emraxxor.ivip.common.filter.Filter;
import com.github.emraxxor.ivip.common.scroll.DefaultScrollResponse;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

import static org.springframework.http.HttpStatus.*;

/**
 *
 * @param <ID>
 * @param <DTO>
 * @param <FILTER>
 *
 * @author Attila Barna
 */
public class AbstractContainerBasedCrudController<
        ID extends Serializable,
        DTO extends AbstractBaseDataElement,
        RESPONSE_DTO extends AbstractBaseDataElement,
        FILTER extends Filter,
        SCROLL extends ScrollSourceData
        >  implements ContainerBasedCrudController<ID,DTO,RESPONSE_DTO, FILTER, SCROLL> {

    protected final ContainerBasedCrudService<ID,DTO,RESPONSE_DTO, FILTER, SCROLL> crudService;

    public AbstractContainerBasedCrudController(ContainerBasedCrudService<ID,DTO,RESPONSE_DTO, FILTER, SCROLL>  crudService) {
        this.crudService = crudService;
    }

    @Override
    public ResponseEntity<DTO> create(DTO dto) {
        return new ResponseEntity<>(crudService.create(dto), CREATED);
    }

    @Override
    public ResponseEntity<DefaultScrollResponse<RESPONSE_DTO>> findAll(FILTER filter, SCROLL scroll) {
        return new ResponseEntity<>( crudService.findAll(filter,scroll) , OK );
    }

    @Override
    public ResponseEntity<DTO> findOne(ID id) {
        return new ResponseEntity<>( crudService.findOne(id), OK );
    }

    @Override
    public ResponseEntity<DTO> update(DTO dto, ID id) {
        return new ResponseEntity<>( crudService.update(dto,id), ACCEPTED );
    }

    @Override
    public ResponseEntity<Void> delete(ID id) {
        crudService.delete(id);
        return new ResponseEntity<>(ACCEPTED );
    }
}
