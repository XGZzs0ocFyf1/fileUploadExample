package a.v.g.wordApp.service;

import a.v.g.wordApp.dtos.AuthenticationResponseDto;
import a.v.g.wordApp.dtos.LoginRq;
import a.v.g.wordApp.dtos.RegistrationUserDto;
import a.v.g.wordApp.exceptions.CodeNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    void registrationRequest(RegistrationUserDto request);
    void registrationSubmit(String registrationToken);
    AuthenticationResponseDto authenticate(LoginRq request);
    ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<AuthenticationResponseDto>  submitRegistration(String rt) throws CodeNotFoundException;
}
