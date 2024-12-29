package a.v.g.wordApp.service.tokens;


import a.v.g.wordApp.model.sec.Token;
import a.v.g.wordApp.model.sec.User;
import a.v.g.wordApp.repo.TokenRepository;
import a.v.g.wordApp.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenUtil jwtTokenUtil;

    private final TokenRepository tokenRepository;

    public boolean isValidToken(String token, UserDetails user) {

        String username = jwtTokenUtil.getUsernameFromToken(token);

        boolean isValidToken = tokenRepository.findByAccessToken(token)
                .map(Token::getLoggedOut)
                .map(loggedOut -> !loggedOut)
                .orElse(false);

        return username.equals(user.getUsername())
                && jwtTokenUtil.isTokenExpired(token)
                && isValidToken;
    }





}
