package a.v.g.wordApp.service;

import a.v.g.wordApp.exceptions.CodeNotFoundException;
import a.v.g.wordApp.model.AuthCode;
import a.v.g.wordApp.model.sec.User;
import a.v.g.wordApp.repo.AuthCodeRepository;
import a.v.g.wordApp.repo.UserRepository;
import a.v.g.wordApp.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthCodeMainService implements AuthCodeService {


    @Value("${registration.authCode.lifeTime}")
    private Duration authLifetime;

    private final AuthCodeRepository authCodeRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    @Override
    public AuthCode generateAuthCode(User user) {

        AuthCode authCode = AuthCode.builder()
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .authCode(jwtTokenUtil.generateToken(user.getUsername(), new Date(System.currentTimeMillis() + authLifetime.toMillis())))
                .user(user)
                .wasItUsed(false)
                .build();

        return authCodeRepository.save(authCode);
    }


    @Override
    public void submitAuthCode(String authCode) throws CodeNotFoundException {
        var auth = authCodeRepository.findByAuthCode(authCode).orElseThrow(() -> new CodeNotFoundException("Code not found"));
        boolean tokenActive = !jwtTokenUtil.isTokenExpired(authCode);
        boolean isEnabled = !auth.getWasItUsed();
        if (isEnabled && tokenActive) {
            var user = auth.getUser();
            user.setEmailVerified(true);
            var res = userRepository.save(user);
            System.out.println("user " + res);
        }
    }

    @Override
    public void disableAuthCode(String authCode) {
        var code = authCodeRepository.findByAuthCode(authCode);
        if (code.isPresent()) {
            var aCode = code.get();
            aCode.setWasItUsed(true);
            authCodeRepository.save(aCode);
        }
    }
}
