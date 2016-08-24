package com.stubs.cool_extensions.filter;

/**
 * Created by vikakumar on 8/8/16.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalMappings {

    @JsonProperty("url")
    private String url;
    @JsonProperty("body")
    private String body;
    @JsonProperty("match")
    private String match;
    @JsonProperty("path")
    private String path;
    @JsonProperty("replaceIndex")
    private String replaceIndex;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The body
     */
    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    /**
     * @param body The body
     */
    @JsonProperty("body")
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return The match
     */
    @JsonProperty("match")
    public String getMatch() {
        return match;
    }

    /**
     * @param match The match
     */
    @JsonProperty("match")
    public void setMatch(String match) {
        this.match = match;
    }

    /**
     * @return The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * @param path The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("replaceIndex")
    public String getReplaceIndex() {
        return replaceIndex;
    }

    @JsonProperty("replaceIndex")
    public void setReplaceIndex(String replaceIndex) {
        this.replaceIndex = replaceIndex;
    }

    
}