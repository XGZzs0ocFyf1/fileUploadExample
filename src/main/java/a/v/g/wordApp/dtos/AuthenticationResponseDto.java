package a.v.g.wordApp.dtos;

public record AuthenticationResponseDto(
        String accessToken,
        String refreshToken){

}