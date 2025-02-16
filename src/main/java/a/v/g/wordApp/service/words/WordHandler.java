package a.v.g.wordApp.service.words;

import a.v.g.wordApp.model.words.Word;
import a.v.g.wordApp.model.words.WordTranslation;
import a.v.g.wordApp.repo.WordRepository;
import a.v.g.wordApp.service.ImageGenerationService;
import a.v.g.wordApp.service.tts.TextToSpeechService;
import a.v.g.wordApp.service.words.translation.YcTranslationApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class WordHandler implements WordProcessor {


    private final YcTranslationApi translationService;

    private final TextToSpeechService ttsService;

    private final ImageGenerationService imgService;

    private final WordRepository wordRepository;


    public void translate(String wordToTranslate) {

        var translationFuture = translationService.asyncTranslate(wordToTranslate);
        var ttsFuture = ttsService.asyncSynthesizeAndUploadText(wordToTranslate);
        var imageFuture = imgService.generateAndUploadImage(wordToTranslate);

        var allFutures = CompletableFuture.allOf(translationFuture);
        allFutures.join(); //подрубаемся

        Word w = Word.builder()
                .text(wordToTranslate)
                .build();

        WordTranslation wt = WordTranslation
                .builder()
                .word(w)
                .languageCode("en")
                .translation(translationFuture.join())
                .build();

        w.setTranslations(List.of(wt));
        w.setAudioUrl(ttsFuture.join());
        w.setImageUrl(imageFuture.join());


        wordRepository.save(w);
    }
}
