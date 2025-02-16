package a.v.g.wordApp.model.yc.yart.rq;

import lombok.Builder;

@Builder
public record  YModel (
        String modelUri,
        GenerationOptions generationOptions,
        YMessage[] messages
){}