package com.stubs.cool_extensions.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "global_mappings"
})
public class Mappings {

    @JsonProperty("global_mappings")
    private List<GlobalMappings> globalMappings = new ArrayList<GlobalMappings>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The globalMappings
     */
    @JsonProperty("global_mappings")
    public List<GlobalMappings> getGlobalMappings() {
        return globalMappings;
    }

    /**
     * @param globalMappings The global_mappings
     */
    @JsonProperty("global_mappings")
    public void setGlobalMappings(List<GlobalMappings> globalMappings) {
        this.globalMappings = globalMappings;
    }


}