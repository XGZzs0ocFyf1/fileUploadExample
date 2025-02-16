package a.v.g.wordApp.controllers.experiments;


import a.v.g.wordApp.service.tts.TextToSpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/tts")
@RequiredArgsConstructor
public class TextToSpeechController {

    private final TextToSpeechService textToSpeechService;

//    @PostMapping("/synthesize")
//    public String synthesize(@RequestParam String text) {
//        String outputFilePath = "D:\\audio\\output.wav";
//        textToSpeechService.synthesizeText(text, outputFilePath);
//        return "Synthesis in progress. Audio will be saved to: " + outputFilePath;
//    }


    @PostMapping("/synthesize")
    public String synthesize(@RequestParam String text) {
        String outputFilePath = "D:\\audio\\output.wav";
        var audio = textToSpeechService.asyncSynthesizeAndUploadText(text);
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            var data = audio.join();
            //TODO тут вот по идее можно в s3 записать
            System.out.println("s3 path: " + data);
           // fos.write(data); //TODO
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Synthesis in progress. Audio will be saved to: " + outputFilePath;
    }



}
