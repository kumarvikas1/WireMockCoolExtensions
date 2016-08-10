package com.stubs.cool_extensions.transformer;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.stubs.cool_extensions.filter.FilterBody;
import com.stubs.cool_extensions.glue.LogicResolver;
import com.stubs.cool_extensions.response.AbstractResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by vikakumar on 6/12/16.
 */
@Component("coolExtensionsTransformer")
public class CoolExtensionsTransformer extends AbstractTransformer {

    final List<AbstractResponseGenerator> responseGeneratorList;

    @Autowired
    CoolExtensionsTransformer(List<AbstractResponseGenerator> responseGeneratorList) {
        this.responseGeneratorList = responseGeneratorList;
    }


    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource) {
        String body = getBody(responseDefinition, fileSource);
        FilterBody filterBody = new FilterBody.Builder().FileSource(fileSource).LogicResolver(getLogicResolver(body, request))
                .Request(request).ResponseDefination(responseDefinition).Body(body).build();
        return isResponeGeneratorsUser(request).isPresent() ? isResponeGeneratorsUser(request).get().getResponse() : filterBody.getFilterBody();
    }


    private Optional<AbstractResponseGenerator> isResponeGeneratorsUser(Request request) {
        return responseGeneratorList.stream().filter(f -> f.applies(request)).findFirst();
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
