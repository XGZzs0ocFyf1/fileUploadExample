package a.v.g.wordApp.dtos;

public record RegistrationUserDto(
     String username,
     String password,
     String confirmPassword,
     String email
){}

