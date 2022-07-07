package com.github.emraxxor.ivip.common.es.type;

import com.github.emraxxor.ivip.common.es.AbstractBaseDataElement;
import com.github.emraxxor.ivip.common.es.ESExtendedDataElement;
import com.google.gson.ExclusionStrategy;
import java.lang.reflect.Type;
import java.util.List;

/**
 *
 * @author Attila Barna
 *
 * @param <T>
 */
public interface ESSimpleResquestElement<TDATAELEMENT extends AbstractBaseDataElement, T extends ESExtendedDataElement<TDATAELEMENT>> {

     String index();

     String type();

     String id();

     String routing();

     void routing(String r);

     T elem();

     TDATAELEMENT data();

     void response( TDATAELEMENT data);

     Type dataType();

     List<ExclusionStrategy> exclusionStrategies();
}
