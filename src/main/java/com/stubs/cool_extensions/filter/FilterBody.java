package com.stubs.cool_extensions.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import com.google.common.base.Joiner;
import com.stubs.cool_extensions.glue.JavaScriptHelper;
import com.stubs.cool_extensions.glue.Logic;
import com.stubs.cool_extensions.glue.LogicResolver;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vikakumar on 8/9/16.
 */
public class FilterBody {

    private Response response;
    private Request request;
    private FileSource fileSource;
    private String body;
    private LogicResolver logicResolver;

    private FilterBody(Builder builder) {
        this.response = builder.response;
        this.request = builder.request;
        this.fileSource = builder.fileSource;
        this.body = builder.body;
        this.logicResolver = builder.logicResolver;
    }

    public Response getFilterBody() {
        return filterBody();
    }


    private void updateBodyGlobally(GlobalMappings globalMappings) {
        Matcher matcher = getMatcherBody(globalMappings);
        String value = Optional.ofNullable(globalMappings.getPath()).isPresent() ? getPathValue(globalMappings) : getBodyValue(globalMappings);
        while (matcher.find()) {
            body = body.replaceAll(matcher.group(), matcher.group().
                    replaceAll(matcher.group(Integer.valueOf(Optional.ofNullable(globalMappings.getReplaceIndex()).orElse("1"))), value));
        }
    }

    private Matcher getMatcherBody(GlobalMappings globalMappings) {
        return Pattern.compile(globalMappings.getMatch()).matcher(body);
    }

    private String getBodyValue(GlobalMappings globalMappings) {
        Matcher matcher = Pattern.compile(globalMappings.getInject(), Pattern.DOTALL).matcher(request.getBodyAsString());
        Matcher matcherRes = getMatcherBody(globalMappings);
        matcher.find();
        matcherRes.find();
        return Optional.ofNullable(globalMappings.getInjectMatch()).isPresent() ?
                getInjectedUpdated(globalMappings, matcher.group(Integer.valueOf(globalMappings.getInjectIndex())), matcherRes.group(Integer.valueOf(globalMappings.getInjectReplaceIndex())))
                : matcher.group(Integer.valueOf(globalMappings.getInjectIndex()));
    }

    private String getInjectedUpdated(GlobalMappings globalMappings, String retval, String replace) {
        return retval.replaceAll(globalMappings.getInjectMatch(), Joiner.on("").join(globalMappings.getInjectReplacePrefix(), replace,
                globalMappings.getInjectReplaceSuffix()));
    }

    private String getPathValue(GlobalMappings globalMappings) {
        return body.startsWith("<") ? JavaScriptHelper.getXMLValue(request, globalMappings.getPath()) :
                JavaScriptHelper.getJSONValue(request, globalMappings.getPath());
    }


    private Response filterBody() {
        applyGlobalMappings();
        if (isBodyPresent() && body.contains("#")) {
            List<Method> methods = Arrays.asList(LogicResolver.class.getMethods());
            Arrays.asList(LogicResolver.class.getMethods()).stream().filter(getMatchingMethod())
                    .sorted(getSortedAnnonation().reversed())
                    .forEach(method -> setBody(method));
        }
        return response;
    }

    private boolean isBodyPresent() {
        return Optional.ofNullable(body).isPresent();
    }

    private void applyGlobalMappings() {
        if (isBodyPresent() && isGlobalMappingConfigExist())
            try {
                List<GlobalMappings> globalMappings = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).readerFor(new TypeReference<List<GlobalMappings>>() {
                }).withRootName("global_mappings").readValue(getMappings());

                globalMappings.stream().filter(filterMappings(request)).forEach(this::updateBodyGlobally);
                logicResolver.setBody(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private Predicate<GlobalMappings> filterMappings(Request request) {
        return m -> isMatch(Optional.ofNullable(m.getUrl()).orElse(".*"), request.getUrl())
                && isMatch(Optional.ofNullable(m.getBody()).orElse(".*"), request.getBodyAsString());
    }

    private boolean isMatch(String regex, String body) {
        return Pattern.compile(regex, Pattern.DOTALL).matcher(body).find();
    }

    private boolean isGlobalMappingConfigExist() {
        return new PathMatchingResourcePatternResolver(this.getClass().getClass().getClassLoader()).getResource("mappingsResponse/globalMappings.json").exists();
    }

    private String getMappings() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClass().getClassLoader());
        return getString(resolver.getResource("mappingsResponse/globalMappings.json"));
    }

    private String getString(Resource f) {
        try {
            return IOUtils.toString(f.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Conditions can't be parsed");
        }
    }

    private Comparator<Method> getSortedAnnonation() {
        return Comparator.comparingInt(f -> f.getAnnotation(Logic.class).exp().length());
    }

    private void setBody(Method method) {
        Response retval = response;
        try {
            String exp = method.getAnnotation(Logic.class).exp();
            body = (String) method.invoke(logicResolver, exp);
            logicResolver.setBody(body);
            response = retval.response().body(body).headers(response.getHeaders()).statusMessage(response.getStatusMessage()).build();
        } catch (Exception w) {
            w.printStackTrace();
        }

    }


    private Predicate<Method> getMatchingMethod() {
        return method -> {
            Matcher matcher = Pattern.compile(getAnnotation(method), Pattern.DOTALL).matcher(body);
            return matcher.find();
        };
    }

    private String getAnnotation(Method method) {
        return Optional.ofNullable(method.getAnnotation(Logic.class)).isPresent() ? method.getAnnotation(Logic.class).exp() : "&&&&";
    }


    public static class Builder {
        private Response response;
        private Request request;
        private FileSource fileSource;
        private String body;
        private LogicResolver logicResolver;

        public Builder Response(Response response) {
            this.response = response;
            return this;
        }

        public Builder Request(Request request) {
            this.request = request;
            return this;
        }

        public Builder FileSource(FileSource fileSource) {
            this.fileSource = fileSource;
            return this;
        }

        public Builder Body(String body) {
            this.body = body;
            return this;
        }

        public Builder LogicResolver(LogicResolver logicResolver) {
            this.logicResolver = logicResolver;
            return this;
        }

        public FilterBody build() {
            return new FilterBody(this);
        }
    }

}
