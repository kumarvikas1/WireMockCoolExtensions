package com.stubs.cool_extensions.response;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

/**
 * Created by vikakumar on 6/19/16.
 */
public abstract class AbstractResponseGenerator extends ResponseDefinition {

    public abstract boolean applies(Request request);

    public abstract ResponseDefinition getResponse();
}
