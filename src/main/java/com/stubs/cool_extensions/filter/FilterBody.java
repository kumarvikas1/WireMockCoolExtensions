package com.stubs.cool_extensions.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
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

    private ResponseDefinition responseDefinition;
    private Request request;
    private FileSource fileSource;
    private String body;
    private LogicResolver logicResolver;

    private FilterBody(Builder builder) {
        this.responseDefinition = builder.responseDefinition;
        this.request = builder.request;
        this.fileSource = builder.fileSource;
        this.body = builder.body;
        this.logicResolver = builder.logicResolver;
    }

    public ResponseDefinition getFilterBody() {
        return filterBody();
    }


    private void updateBodyGlobally(GlobalMappings globalMappings) {
        String value = body.startsWith("<") ? JavaScriptHelper.getXMLValue(request, globalMappings.getPath()) :
                JavaScriptHelper.getJSONValue(request, globalMappings.getPath());
        Matcher matcher = Pattern.compile(globalMappings.getMatch()).matcher(body);
        while (matcher.find()) {
            body = body.replaceAll(matcher.group(), matcher.group().
                    replaceAll(matcher.group(Integer.valueOf(Optional.ofNullable(globalMappings.getReplaceIndex()).orElse("1"))), value));
        }
    }


    private ResponseDefinition filterBody() {
        ResponseDefinition retval = responseDefinition;
        applyGlobalMappings();
        if (isBodyPresent() && body.contains("#")) {
            List<Method> methods = Arrays.asList(LogicResolver.class.getMethods());
            Arrays.asList(LogicResolver.class.getMethods()).stream().filter(getMatchingMethod())
                    .sorted(getSortedAnnonation().reversed())
                    .forEach(method -> setBody(method, retval));
        }
        return retval;
    }

    private boolean isBodyPresent() {
        return Optional.ofNullable(body).isPresent();
    }

    private void applyGlobalMappings() {
        if (isBodyPresent() && isGlobalMappingConfigExist())
            try {
                List<GlobalMappings> globalMappings = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).readerFor(new TypeReference<List<GlobalMappings>>() {
                }).withRootName("global_mappings").readValue(getMappings());

                globalMappings.stream().filter(f -> request.getUrl().matches(f.getUrl())).forEach(this::updateBodyGlobally);
                logicResolver.setBody(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private void setBody(Method method, ResponseDefinition retval) {
        try {
            String exp = method.getAnnotation(Logic.class).exp();
            body = (String) method.invoke(logicResolver, exp);
            logicResolver.setBody(body);
            retval.setBody(body);
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
        private ResponseDefinition responseDefinition;
        private Request request;
        private FileSource fileSource;
        private String body;
        private LogicResolver logicResolver;

        public Builder ResponseDefination(ResponseDefinition responseDefinition) {
            this.responseDefinition = responseDefinition;
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
