package a.v.g.wordApp.controllers.testControllers;


import a.v.g.wordApp.service.yandx.IamTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RequestMapping("/test")
@RestController
@RequiredArgsConstructor
public class TestController {

    //just for test some methods
    private final IamTokenService service;




    @GetMapping("/1")
    public String doIt(){
       String iamToken = null;
        try {
            iamToken = service.getIamToken();
        } catch (IOException e) {
            System.out.println("Can't read json file with key");
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Bad algorithm for key");
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            System.out.println("Bad key spec for key");
        }
        return iamToken;
    }

}
