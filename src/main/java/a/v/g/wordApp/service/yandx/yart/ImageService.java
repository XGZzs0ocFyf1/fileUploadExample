package a.v.g.wordApp.service.yandx.yart;

import java.util.concurrent.CompletableFuture;

public interface ImageService {
    CompletableFuture<String> generateImgAndSaveIt(String text);
}
