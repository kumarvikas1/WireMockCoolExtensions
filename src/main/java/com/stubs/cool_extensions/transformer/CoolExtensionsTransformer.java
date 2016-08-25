package com.stubs.cool_extensions.transformer;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.stubs.cool_extensions.response.AbstractResponseGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by vikakumar on 8/25/16.
 */
@Component("coolExtensionsTransformer")
public class CoolExtensionsTransformer extends AbstractExtensionsTransformer {

    final List<AbstractResponseGenerator> responseGeneratorList;

    @Autowired
    CoolExtensionsTransformer(List<AbstractResponseGenerator> responseGeneratorList) {
        this.responseGeneratorList = responseGeneratorList;
    }

    @Override
    public String getName() {
        return "Cool Extensions Transformer";
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource, Parameters parameters) {
        Optional<AbstractResponseGenerator> responseGenerator = responseGeneratorList.stream().filter(f -> f.applies(request)).findFirst();
        return responseGenerator.isPresent() ? ResponseDefinitionBuilder.like(responseDefinition).withBody(responseGenerator.get().getResponse().getBody()).build()
                : responseDefinition;
    }

}
