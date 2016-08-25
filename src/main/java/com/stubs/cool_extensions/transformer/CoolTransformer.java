package com.stubs.cool_extensions.transformer;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.stubs.cool_extensions.filter.FilterBody;
import com.stubs.cool_extensions.freemaker.ResponseMaker;
import com.stubs.cool_extensions.glue.LogicResolver;
import org.springframework.stereotype.Component;

/**
 * Created by vikakumar on 6/12/16.
 */
@Component("coolTransformer")
public class CoolTransformer extends AbstractTransformer {

    @Override
    public String getName() {
        return "Cool Transformer";
    }

    @Override
    public Response transform(Request request, Response response, FileSource fileSource, Parameters parameters) {
        String body = response.getBodyAsString();
        response = ResponseMaker.of(response, request).evaluateLogic();
        return new FilterBody.Builder().FileSource(fileSource).LogicResolver(getLogicResolver(body, request))
                .Request(request).Response(response).Body(body).build().getFilterBody();
    }


    protected LogicResolver getLogicResolver(String Body, Request request) {
        return new LogicResolver(Body, request);
    }

}
