package a.v.g.wordApp.model.yc.yart.rs;

import com.fasterxml.jackson.annotation.JsonProperty;


public record GenerationResponse(
        @JsonProperty("@type") String theType,
        String image,
        String modelVersion
) {}