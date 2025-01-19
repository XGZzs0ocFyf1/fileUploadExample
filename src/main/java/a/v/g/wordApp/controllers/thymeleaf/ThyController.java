package a.v.g.wordApp.controllers.thymeleaf;

import a.v.g.wordApp.service.SimpleAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class ThyController {

    private final SimpleAuthService authService;

    @GetMapping("/main_page")
    public String mainPage(){
        return "main_page";
    }

    @GetMapping("/login/direct")
    public String loginForm(){
        return "/login";
    }

    @PostMapping("/login/direct")
    public String loginDirect(
            @ModelAttribute("username") String login, @ModelAttribute("password") String password,
            RedirectAttributes redirectAttributes){

        System.out.println("Login direct " + login + " " + password);
        var isAuth = authService.authenticate(login, password);
        if (!isAuth) {
            var errorMsg = "Неверный логин или пароль";
            redirectAttributes.addAttribute("errorMsg", errorMsg);
            return "redirect:/login/error";
        }
        return "redirect:/main_page";
    }

    @GetMapping("/login/error")
    public String loginError(@RequestParam("errorMsg") String msg, Model model){
        model.addAttribute("errorMsg", msg);
        return "/loginError";
    }


}
