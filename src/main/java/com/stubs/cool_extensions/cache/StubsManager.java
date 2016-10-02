package com.stubs.cool_extensions.cache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.Request;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by vikakumar on 9/28/16.
 */
public class StubsManager {
    private static List<Cache> cacheList;
    private static final StubsManager cacheManager = new StubsManager();

    private static Map<String, String> cache = new HashMap<>();

    StubsManager() {
        load();
    }


    public String getCache(String key) {
        return cache.get(key);
    }

    public void addCache(Request key, String value) {
        cacheList.stream().filter(filterCaching(key)).forEach(f -> cache.put(f.getPattern(), value));

    }

    private Predicate<Cache> filterCaching(Request request) {
        return t -> "URL".matches(t.getType()) ? request.getUrl().matches(t.getPattern()) :
                request.getBodyAsString().matches(t.getPattern());
    }

    private void load() {
        try {
            cacheList = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).readerFor(new TypeReference<List<Cache>>() {
            }).withRootName("cache").readValue(getCacheConfig());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCacheConfig() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClass().getClassLoader());
        return getString(resolver.getResource("mappingsResponse/cacheConfig.json"));
    }

    private String getString(Resource f) {
        try {
            return IOUtils.toString(f.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Conditions can't be parsed");
        }
    }

    public static StubsManager get() {
        return cacheManager;
    }
}
