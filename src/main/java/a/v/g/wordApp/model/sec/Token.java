package a.v.g.wordApp.model.sec;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String accessToken;
    private String refreshToken;
    private Boolean loggedOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Token logOut(Token this){
      return new Token(
                this.tokenId,
                this.accessToken,
                this.refreshToken,
                false,
                this.user
        );
    }
}
