package a.v.g.wordApp.controllers.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

    //TODO add wordService

    @GetMapping("/main_page_OLD")
    public String mainPage() {
        return "mainPage";
    }



}
