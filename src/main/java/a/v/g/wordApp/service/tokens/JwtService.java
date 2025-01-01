package a.v.g.wordApp.service.tokens;

import a.v.g.wordApp.model.sec.User;

public interface JwtService {
     String generateAccessToken(String username);
     String generateRefreshToken(String username);
     boolean isValidToken(String token, String username);
     String getUsernameFromToken(String token);
     void revokeAllToken(User user);
     void saveUserToken(String accessToken, String refreshToken, User user);
     void saveRegistrationToken(String registrationToken,  User user);
}
