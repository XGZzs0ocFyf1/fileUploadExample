package a.v.g.wordApp.service.s3;

import a.v.g.wordApp.exceptions.FileNotFoundInBucketException;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface YCS3Api {
    CompletableFuture<String> asyncUploadFile(String folderName, String filename, byte[] data, String contentType);
    String uploadFile(String folderName, String filename, byte[] data, String contentType);
    ResponseEntity<byte[]> downloadFileByName(String key) throws FileNotFoundInBucketException;
    void testIt() throws FileNotFoundInBucketException; //TODO
}
