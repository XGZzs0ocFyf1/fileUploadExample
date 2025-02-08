package a.v.g.wordApp.controllers.s3;

import a.v.g.wordApp.exceptions.FileNotFoundInBucketException;
import a.v.g.wordApp.service.s3.S3Service;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
@RequestMapping("/photos")
@Tag(name = "Photos")
@ApiResponses(@ApiResponse(responseCode = "200", useReturnTypeSchema = true))
@RequiredArgsConstructor
public class ImageController {

    private final S3Service s3Service;

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestParam MultipartFile photo) throws IOException {
        return s3Service.uploadFile(photo.getOriginalFilename(), photo.getBytes(), photo.getContentType());
    }

    @GetMapping
    public ResponseEntity<byte[]> downloadFile(@RequestParam String key) throws FileNotFoundInBucketException {
        return s3Service.downloadFileByName(key);
    }

    @GetMapping("/test")
    public String rnd(@RequestParam Optional<String> key) throws FileNotFoundInBucketException {
        System.out.println("key = " + key);
        s3Service.testIt();
        return "";
    }

}