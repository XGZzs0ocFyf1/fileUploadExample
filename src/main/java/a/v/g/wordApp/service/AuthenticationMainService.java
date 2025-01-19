package a.v.g.wordApp.service;

import a.v.g.wordApp.dtos.AuthenticationResponseDto;
import a.v.g.wordApp.dtos.LoginRq;
import a.v.g.wordApp.dtos.RegistrationUserDto;
import a.v.g.wordApp.exceptions.CodeNotFoundException;
import a.v.g.wordApp.model.sec.ROLE_NAME;
import a.v.g.wordApp.model.sec.Role;
import a.v.g.wordApp.model.sec.User;
import a.v.g.wordApp.repo.RoleRepository;
import a.v.g.wordApp.repo.UserRepository;
import a.v.g.wordApp.service.tokens.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationMainService implements AuthenticationService{
    private final UserRepository userRepository;

    private final JwtService jwtMainService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final AuthCodeService authCodeService;

    public Role createNewUser(Optional<Role> in) {
        if (in.isPresent()){
            System.out.println(in + "is present");
            return in.get();
        }else {
            return roleRepository.save(new Role(ROLE_NAME.USER.toString()));
        }

    }

    public void registrationRequest(RegistrationUserDto request) {
        Optional<Role> mbROle = roleRepository.findByName(ROLE_NAME.USER.toString());
        var userRole = mbROle.orElse(createNewUser(mbROle));

//                roleRepository.save(new Role(ROLE_NAME.USER.toString())) // TODO?


        User user = User
                .builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(List.of(userRole))
                .build();
        var newUser = userRepository.save(user);

        var authCode = authCodeService.generateAuthCode(user);

        emailService.sendRegistrationEmail(user.getUsername(), newUser.getEmail(), authCode.getAuthCode());
    }

    @Override
    public void registrationSubmit(String registrationToken) {

    }


    public AuthenticationResponseDto authenticate(LoginRq request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        User user = userRepository.findByUsername(request.username()).orElseThrow();
        String accessToken = jwtMainService.generateAccessToken(user.getUsername());
        String refreshToken = jwtMainService.generateRefreshToken(user.getUsername());

        jwtMainService.revokeAllToken(user);
        jwtMainService.saveUserToken(accessToken, refreshToken, user);
        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request, HttpServletResponse response) {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtMainService.getUsernameFromToken(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        if (jwtMainService.isValidToken(token, user.getUsername())) {
            String accessToken = jwtMainService.generateAccessToken(user.getUsername());
            String refreshToken = jwtMainService.generateRefreshToken(user.getUsername());

            jwtMainService.revokeAllToken(user);
            jwtMainService.saveUserToken(accessToken, refreshToken, user);
            return new ResponseEntity<>(new AuthenticationResponseDto(accessToken, refreshToken), HttpStatus.OK);

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Override
    public ResponseEntity<AuthenticationResponseDto>  submitRegistration(String rt) throws CodeNotFoundException {
        authCodeService.submitAuthCode(rt);
        authCodeService.disableAuthCode(rt);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
























