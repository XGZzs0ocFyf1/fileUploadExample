package a.v.g.wordApp.controllers;

import a.v.g.wordApp.dtos.RegistrationUserDto;
import a.v.g.wordApp.exceptions.CodeNotFoundException;
import a.v.g.wordApp.service.AuthenticationService;
import a.v.g.wordApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userMainService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public ResponseEntity<String> register(
            @RequestBody RegistrationUserDto registrationDto) {

        if(userMainService.existsByUsername(registrationDto.username())) {
            return ResponseEntity.badRequest().body("Имя пользователя уже занято");
        }

        if(userMainService.existsByEmail(registrationDto.email())) {
            return ResponseEntity.badRequest().body("Email уже занят");
        }

        authenticationService.registrationRequest(registrationDto);

        return ResponseEntity.ok("Вам будет отправлено письмо с подтверждением регистрации.");
    }


    @GetMapping("/registration/submit")
    public ResponseEntity<String> submit(@RequestParam String rt) throws CodeNotFoundException {
        authenticationService.submitRegistration(rt);
        return ResponseEntity.ok("Вы подтвердили адрес электронной почты. Спасибо за регистрацию!");

    }

}




























