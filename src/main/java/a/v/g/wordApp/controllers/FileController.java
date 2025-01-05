package a.v.g.wordApp.controllers;

import a.v.g.wordApp.model.FileDB;
import a.v.g.wordApp.response.ResponseFile;
import a.v.g.wordApp.response.ResponseMessage;
import a.v.g.wordApp.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:8081")
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.store(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId())
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    //так файл ты скачаешь
    @GetMapping("/files/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileId) {
       return storageService.getFile(fileId) //здесь тип опциональный (т.е. может быть Null внутри)
               .map(dbFIle -> //мап это как будто ты ифчик делаешь на контейнере изменяя его содержимое (те если внутри чот есть, применится вот этот код)
                       ResponseEntity.ok() //просто красивая обертка для ответа
                               .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"") // без этой строчки вместо закачки у тебя будет кусок как будто ты изображение в текстовом редакторе открыл
                               .body(dbFIle.getData()) // в содержимое ответа кладем массив байтов
               ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); //если внутри optional null те он пустой, то мы возвращаем ошибку 404 (

    }

    //так картинку ты отрендеришь
    @GetMapping(value ="/images/{fileId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String fileId) {
        return storageService.getFile(fileId)
                .map(dbFIle -> ResponseEntity.ok().body(dbFIle.getData()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


}