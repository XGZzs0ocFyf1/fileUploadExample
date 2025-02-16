package a.v.g.wordApp.model.yc.yart.rq;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenerationOptions {
    private String seed;
    private AspectRatio aspectRatio;
}