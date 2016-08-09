package com.stubs.cool_extensions.transformer;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.stubs.cool_extensions.config.ConfigResolver;
import com.stubs.cool_extensions.filter.FilterBody;
import com.stubs.cool_extensions.glue.LogicResolver;
import com.stubs.cool_extensions.response.AbstractResponseGenerator;

import java.util.Optional;

/**
 * Created by vikakumar on 6/12/16.
 */
public class CoolExtensionsTransformer extends AbstractTransformer {


    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource) {
        String body = getBody(responseDefinition, fileSource);
        FilterBody filterBody = new FilterBody.Builder().FileSource(fileSource).LogicResolver(getLogicResolver(body, request))
                .Request(request).ResponseDefination(responseDefinition).Body(body).build();
        Optional<? extends AbstractResponseGenerator> responseGenerator = ConfigResolver.getResolver().getResponse(request);
        return responseGenerator.isPresent() ? responseGenerator.get().getResponse() : filterBody.getFilterBody();
    }

    private String getBody(ResponseDefinition responseDefinition, FileSource fileSource) {
        return Optional.ofNullable(responseDefinition.getBodyFileName()).isPresent() ?
                new String(fileSource.getBinaryFileNamed(responseDefinition.getBodyFileName()).readContents())
                : responseDefinition.getBody();
    }

    protected LogicResolver getLogicResolver(String Body, Request request) {
        return new LogicResolver(Body, request);
    }

    public String name() {
        return this.getClass().getSimpleName();
    }
}
