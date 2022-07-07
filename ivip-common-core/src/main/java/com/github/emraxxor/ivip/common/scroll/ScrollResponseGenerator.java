package com.github.emraxxor.ivip.common.scroll;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * @author Attila Barna
 */
@Component
@Primary
@RequiredArgsConstructor
public class ScrollResponseGenerator {


    private final WebApplicationContext context;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T extends DefaultScrollResponse<?>, V extends AbstractScrollResponse> T generate(@NotNull T o, @NotNull V source, Object... params) {
        o.source(source);

        if ( params != null )
            o.params(Arrays.asList(params));

        o.context(context);
        o.postInit();
        o.queryInitialization();
        o.executeQuery();
        o.onQueryComplete();
        o.beforeDestroy();
        return o;
    }

}
