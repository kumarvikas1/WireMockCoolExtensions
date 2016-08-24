package com.stubs.cool_extensions.matcher;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Created by vikakumar on 8/24/16.
 */
@Component("requestMatcher")
public class RequestBodyMatcher extends RequestMatcherExtension {

    @Override
    public MatchResult match(Request request) {
        return super.match(request);
    }

    @Override
    public String getName() {
        return "body-matching";
    }

    @Override
    public MatchResult match(Request request, Parameters parameters) {

        return MatchResult.of(Pattern.compile(parameters.getString("bodyPattern"), Pattern.DOTALL).matcher(request.getBodyAsString()).find());
    }
}
