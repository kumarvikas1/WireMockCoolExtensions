package com.stubs.cool_extensions.config;

import com.github.tomakehurst.wiremock.http.Request;
import com.stubs.cool_extensions.response.AbstractResponseGenerator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by vikakumar on 6/25/16.
 */
public class ConfigResolver {
    private static final ConfigResolver configResolver = new ConfigResolver();
    private static final String BUILD_CONF = "conf/response";
    private static final Config CONFIG = ConfigFactory.load(BUILD_CONF);

    private ConfigResolver() {

    }

    public static Config getConfig() {
        return CONFIG;
    }

    public static ConfigResolver getResolver() {
        return configResolver;
    }

    public List<? extends AbstractResponseGenerator> getResponseGenerator() {
        return CONFIG.getStringList("response").stream().map(f -> {
            try {
                return (AbstractResponseGenerator) Class.forName(f).newInstance();
            } catch (Exception e) {
                throw new IllegalStateException("Class can't be instantiated");
            }
        }).collect(Collectors.toList());
    }


    public Optional<? extends AbstractResponseGenerator> getResponse(Request request) {
        return getResponseGenerator().stream().filter(f -> f.applies(request)).findFirst();
    }
}
