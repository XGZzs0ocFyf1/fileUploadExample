package a.v.g.wordApp.model.sec;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "tokens")
@Data
public class Token {

    private Long tokenId;
    private String accessToken;
    private String refreshToken;
    private Boolean loggedOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
