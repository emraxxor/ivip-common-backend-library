package com.github.emraxxor.ivip.common.configuration;


/**
 * 
 * @author Attila Barna
 *
 * @param <CLIENT>
 */
public interface DefaultElasticsearchConnection<CLIENT, TEMPLATE> {

	public CLIENT client();
	
	public TEMPLATE template();
	
}
