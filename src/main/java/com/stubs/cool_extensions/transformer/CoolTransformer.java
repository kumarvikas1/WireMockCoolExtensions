package com.stubs.cool_extensions.transformer;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.stubs.cool_extensions.cache.StubsManager;
import com.stubs.cool_extensions.filter.FilterBody;
import com.stubs.cool_extensions.freemaker.ResponseMaker;
import com.stubs.cool_extensions.glue.LogicResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by vikakumar on 6/12/16.
 */
@Component("coolTransformer")
public class CoolTransformer extends AbstractTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoolTransformer.class);

    @Override
    public String getName() {
        return "Cool Transformer";
    }

    @Override
    public Response transform(Request request, Response response, FileSource fileSource, Parameters parameters) {
        LOGGER.info("Request body {}", request.getBodyAsString());
        String body = response.getBodyAsString();
        response = ResponseMaker.of(response, request).evaluateLogic();
        Response retval = new FilterBody.Builder().FileSource(fileSource).LogicResolver(getLogicResolver(body, request))
                .Request(request).Response(response).Body(body).build().getFilterBody();
        StubsManager.get().addCache(request, retval.getBodyAsString());
        LOGGER.info("Response " + retval.getBodyAsString());
        return retval;
    }


    protected LogicResolver getLogicResolver(String Body, Request request) {
        return new LogicResolver(Body, request);
    }

}
