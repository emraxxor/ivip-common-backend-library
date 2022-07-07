package com.github.emraxxor.ivip.common.crud;

import com.github.emraxxor.ivip.common.entity.BaseEntityIF;
import lombok.*;

import javax.persistence.Id;

/**
 *
 * @author Attila Barna
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseIndex implements BaseEntityIF<String> {

    @Id
    @EqualsAndHashCode.Include
    protected String id;

}
