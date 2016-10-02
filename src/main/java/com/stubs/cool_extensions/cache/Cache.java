package com.stubs.cool_extensions.cache;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "urlPattern",
        "bodyPattern"
})
public class Cache {

    @JsonProperty("Pattern")
    private String Pattern;
    @JsonProperty("Type")
    private String type;

    public String getPattern() {
        return Pattern;
    }

    public String getType() {
        return type;
    }

    public void setPattern(String pattern) {
        Pattern = pattern;
    }

    public void setType(String type) {
        this.type = type;
    }
}
