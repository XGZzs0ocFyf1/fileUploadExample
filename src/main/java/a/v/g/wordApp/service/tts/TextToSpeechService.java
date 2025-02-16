package a.v.g.wordApp.service.tts;

import java.util.concurrent.CompletableFuture;

public interface TextToSpeechService {


    CompletableFuture<String> asyncSynthesizeAndUploadText(String text);
    void synthesizeText(String text, String outputFilePath);
}
