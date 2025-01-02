package a.v.g.wordApp.dtos;

import a.v.g.wordApp.model.sec.Token;
import lombok.Getter;

@Getter
public class TokenDTO {
    private Long tokenId;
    private String accessToken;
    private String refreshToken;
    private Boolean loggedOut;
    private String username;  // Вместо пользователя

    public TokenDTO(Token token) {
        this.tokenId = token.getTokenId();
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
        this.loggedOut = token.getLoggedOut();
        this.username = token.getUser().getUsername();  // Логика получения имени пользователя
    }
}