package com.stubs.cool_extensions.transformer;

/**
 * Created by vikakumar on 6/29/16.
 */

import com.github.tomakehurst.wiremock.extension.ResponseTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.stubs.cool_extensions.glue.LogicResolver;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractTransformer extends ResponseTransformer {

    protected abstract LogicResolver getLogicResolver(String Body, Request request);
}
