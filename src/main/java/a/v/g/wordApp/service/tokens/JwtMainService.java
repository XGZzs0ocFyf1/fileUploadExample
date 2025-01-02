package a.v.g.wordApp.service.tokens;


import a.v.g.wordApp.dtos.TokenDTO;
import a.v.g.wordApp.model.sec.Token;
import a.v.g.wordApp.model.sec.User;
import a.v.g.wordApp.repo.TokenRepository;
import a.v.g.wordApp.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtMainService implements JwtService {

    @Value("${jwt.lifetime}")
    private Duration jwtExpirationInMs;

    @Value("${jwt.refreshExpiration}")
    private Duration refreshExpirationInMs;

    private final JwtTokenUtil jwtTokenUtil;
    private final TokenRepository tokenRepository;

    @Override
    public String generateAccessToken(String username) {
        return jwtTokenUtil.generateToken(username, new Date(System.currentTimeMillis() + jwtExpirationInMs.toMillis()));
    }

    @Override
    public String generateRefreshToken(String username) {
        return jwtTokenUtil.generateToken(username, new Date(System.currentTimeMillis() + refreshExpirationInMs.toMillis()));
    }

    @Transactional
    public boolean isValidToken(String token, String userName) {

        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(token);
        var isNotExpired = !jwtTokenUtil.isTokenExpired(token);
        var sameName = usernameFromToken.equals(userName);

        var isValidToken = tokenRepository.findByAccessToken(token)
                .map(Token::getLoggedOut)
                .map(loggedOut -> !loggedOut)
                .orElse(false);

        return sameName && isNotExpired && isValidToken;
    }


    @Override
    public String getUsernameFromToken(String token) {
        return jwtTokenUtil.parseClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public void revokeAllToken(User user) {
        var tokens = tokenRepository.findAllAccessTokenByUser(user.getUserId());
        tokens.forEach(t -> t.setLoggedOut(true));
        tokenRepository.saveAll(tokens); //тут могут токены не сохраниться из-за дефолтных equals и hashcode
    }

    @Override
    public void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .loggedOut(false)
                .user(user)
                .build();

        tokenRepository.save(token);
    }

    @Override
    public void saveRegistrationToken(String registrationToken, User user) {
        Token t = Token.builder()
                .accessToken(registrationToken)
                .user(user)
                .build();
        tokenRepository.save(t);
    }


}
