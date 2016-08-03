package com.stubs.cool_extensions.transformer;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.stubs.cool_extensions.config.ConfigResolver;
import com.stubs.cool_extensions.glue.Logic;
import com.stubs.cool_extensions.glue.LogicResolver;
import com.stubs.cool_extensions.response.AbstractResponseGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vikakumar on 6/12/16.
 */
public class CoolExtensionsTransformer extends AbstractTransformer {

    private ResponseDefinition responseDefinition;
    private Request request;
    private FileSource fileSource;
    private String Body;

    private void init(Request request, ResponseDefinition responseDefinition, FileSource fileSource) {
        this.responseDefinition = responseDefinition;
        this.request = request;
        this.fileSource = fileSource;
    }

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource fileSource) {
        init(request, responseDefinition, fileSource);
        Optional<? extends AbstractResponseGenerator> responseGenerator = ConfigResolver.getResolver().getResponse(request);
        return responseGenerator.isPresent() ? responseGenerator.get().getResponse() : filterBody();
    }

    private ResponseDefinition filterBody() {
        getBody();
        ResponseDefinition retval = responseDefinition;
        if (Optional.ofNullable(Body).isPresent() && (Body.contains("${") || Body.contains("#"))) {
            List<Method> methods = Arrays.asList(LogicResolver.class.getMethods());
            Arrays.asList(LogicResolver.class.getMethods()).stream().filter(getMatchingMethod(Body))
                    .forEach(method -> setBody(Body, method, retval));
        }
        return retval;
    }

    private void setBody(String body, Method method, ResponseDefinition retval) {
        try {
            String exp = method.getAnnotation(Logic.class).exp();
            Body = (String) method.invoke(getLogicResolver(body, request), exp);
            retval.setBody(Body);
        } catch (Exception w) {
            w.printStackTrace();
        }
    }

    private void getBody() {
        Body = Optional.ofNullable(responseDefinition.getBodyFileName()).isPresent() ?
                new String(fileSource.getBinaryFileNamed(responseDefinition.getBodyFileName()).readContents())
                : responseDefinition.getBody();
    }

    private Predicate<Method> getMatchingMethod(String Body) {
        return method -> {
            Matcher matcher = Pattern.compile(getAnnotation(method), Pattern.DOTALL).matcher(Body);
            return matcher.find();
        };
    }

    private String getAnnotation(Method method) {
        return Optional.ofNullable(method.getAnnotation(Logic.class)).isPresent() ? method.getAnnotation(Logic.class).exp() : "&&&&";
    }

    protected LogicResolver getLogicResolver(String Body, Request request) {
        return new LogicResolver(Body, request);
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

}
