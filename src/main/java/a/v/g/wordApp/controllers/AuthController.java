package a.v.g.wordApp.controllers;

import a.v.g.wordApp.dtos.AuthenticationResponseDto;
import a.v.g.wordApp.dtos.JwtResponse;
import a.v.g.wordApp.dtos.LoginRq;
import a.v.g.wordApp.exceptions.AppError;
import a.v.g.wordApp.service.AuthenticationService;
import a.v.g.wordApp.service.UserService;
import a.v.g.wordApp.service.tokens.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userMainService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    @PostMapping("/auth")
    public  ResponseEntity<?> createAuthToken(@RequestBody LoginRq authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new AppError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
        }
        UserDetails userDetails = userMainService.loadUserByUsername(authRequest.username());
        String token = jwtService.generateAccessToken(userDetails.getUsername());
        return ResponseEntity.ok(new JwtResponse(token));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody LoginRq request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        return authenticationService.refreshToken(request, response);
    }
}
