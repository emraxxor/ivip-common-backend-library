package com.github.emraxxor.ivip.common.configuration;


/**
 * 
 * @author Attila Barna
 *
 * @param <CLIENT>
 */
public interface DefaultElasticsearchConnection<CLIENT, TEMPLATE> {

	CLIENT client();
	
	TEMPLATE template();
	
}
