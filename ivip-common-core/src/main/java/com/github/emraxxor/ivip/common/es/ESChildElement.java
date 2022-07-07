package com.github.emraxxor.ivip.common.es;

public interface ESChildElement<T extends AbstractBaseDataElement> extends ESExtendedDataElement<T> {

    String parent();

}
