package a.v.g.wordApp.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/secured")
    public String securedData() {
        return "Secured data";
    }

        @GetMapping("/unsecured")
    public String unsecuredData() {
        return "Unecured data";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin data";
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/hello")
    public String index() {
        return "Hello World!";
    }
}
