package a.v.g.wordApp.controllers.clientApi;

import a.v.g.wordApp.model.ApiTokenResponse;
import org.springframework.web.bind.annotation.*;

@RestController//("/api")
public class MobileController {


    @PostMapping("/api/token/save")
    public ApiTokenResponse saveToken(@RequestBody String token){
        System.out.println("save token" + token);
        return new ApiTokenResponse(true, "", "");
    }
}
