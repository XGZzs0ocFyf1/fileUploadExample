package a.v.g.wordApp.service;

import a.v.g.wordApp.dtos.AuthenticationResponseDto;
import a.v.g.wordApp.dtos.LoginRq;
import a.v.g.wordApp.dtos.RegistrationUserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    void registrationRequest(RegistrationUserDto request);
    void registrationSubmit();
    AuthenticationResponseDto authenticate(LoginRq request);
    ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response);

}
