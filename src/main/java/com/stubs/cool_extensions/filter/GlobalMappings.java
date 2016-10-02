package com.stubs.cool_extensions.filter;

/**
 * Created by vikakumar on 8/8/16.
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("inject")
    private String inject;
    @JsonProperty("replaceIndex")
    private String replaceIndex;
    @JsonProperty("injectIndex")
    private String injectIndex;
    @JsonProperty("injectReplaceIndex")
    private String injectReplaceIndex;
    @JsonProperty("injectMatch")
    private String injectMatch;
    @JsonProperty("injectReplacePrefix")
    private String injectReplacePrefix;
    @JsonProperty("injectReplaceSuffix")
    private String injectReplaceSuffix;

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

    /**
     * @return The inject
     */
    @JsonProperty("inject")
    public String getInject() {
        return inject;
    }

    /**
     * @param inject The inject
     */
    @JsonProperty("inject")
    public void setInject(String inject) {
        this.inject = inject;
    }

    @JsonProperty("replaceIndex")
    public String getReplaceIndex() {
        return replaceIndex;
    }

    @JsonProperty("replaceIndex")
    public void setReplaceIndex(String replaceIndex) {
        this.replaceIndex = replaceIndex;
    }

    @JsonProperty("injectIndex")
    public String getInjectIndex() {
        return injectIndex;
    }

    @JsonProperty("injectIndex")
    public void setInjectIndex(String injectIndex) {
        this.injectIndex = injectIndex;
    }

    @JsonProperty("injectReplaceIndex")
    public String getInjectReplaceIndex() {
        return injectReplaceIndex;
    }

    @JsonProperty("injectReplaceIndex")
    public void setInjectReplaceIndex(String injectReplaceIndex) {
        this.injectReplaceIndex = injectReplaceIndex;
    }

    @JsonProperty("injectMatch")
    public String getInjectMatch() {
        return injectMatch;
    }

    @JsonProperty("injectMatch")
    public void setInjectMatch(String injectMatch) {
        this.injectMatch = injectMatch;
    }

    @JsonProperty("injectReplacePrefix")
    public String getInjectReplacePrefix() {
        return injectReplacePrefix;
    }

    @JsonProperty("injectReplacePrefix")
    public void setInjectReplacePrefix(String injectReplacePrefix) {
        this.injectReplacePrefix = injectReplacePrefix;
    }

    @JsonProperty("njectReplaceSuffix")
    public String getInjectReplaceSuffix() {
        return injectReplaceSuffix;
    }

    @JsonProperty("njectReplaceSuffix")
    public void setInjectReplaceSuffix(String injectReplaceSuffix) {
        this.injectReplaceSuffix = injectReplaceSuffix;
    }
}