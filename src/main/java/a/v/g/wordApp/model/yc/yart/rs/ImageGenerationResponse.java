package a.v.g.wordApp.model.yc.yart.rs;

public record ImageGenerationResponse(
        String id,
        String description,
        String createdAt,
        String createdBy,
        String modifiedAt,
        boolean done,
        Object metadata,
        GenerationResponse response
) {}
