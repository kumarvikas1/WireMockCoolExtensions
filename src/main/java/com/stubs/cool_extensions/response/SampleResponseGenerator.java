package com.stubs.cool_extensions.response;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import org.springframework.stereotype.Component;

/**
 * Created by vikakumar on 8/10/16.
 */
@Component
public class SampleResponseGenerator extends AbstractResponseGenerator {

    @Override
    public ResponseDefinition getResponse() {
        throw new IllegalStateException("Should never be executed");
    }

    @Override
    public boolean applies(Request request) {
        return false;
    }
}
