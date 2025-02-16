package a.v.g.wordApp.service.words.translation;

import java.util.concurrent.CompletableFuture;

public interface YcTranslationApi {
    CompletableFuture<String> asyncTranslate(String word);
    String translate(String word);
}
