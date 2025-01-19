package a.v.g.wordApp.controllers.experiments;


import a.v.g.wordApp.service.tts.TextToSpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tts")
@RequiredArgsConstructor
public class TextToSpeechController {

    private final TextToSpeechService textToSpeechService;

    @PostMapping("/synthesize")
    public String synthesize(@RequestParam String text) {
        String outputFilePath = "D:\\audio\\output.wav";
        textToSpeechService.synthesizeText(text, outputFilePath);
        return "Synthesis in progress. Audio will be saved to: " + outputFilePath;
    }
}
