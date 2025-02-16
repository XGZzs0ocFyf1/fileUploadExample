package a.v.g.wordApp.service.words.translation;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class YcTranslationApiImpl implements YcTranslationApi {


    @Async
    @Override
    public CompletableFuture<String> asyncTranslate(String word) {
        return CompletableFuture.supplyAsync(() -> translate(word));
    }

    @Override
    public String translate(String word) {
        return "mock translation";
    }
}
