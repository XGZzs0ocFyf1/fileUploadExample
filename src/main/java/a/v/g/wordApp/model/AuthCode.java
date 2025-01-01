package a.v.g.wordApp.model;

import a.v.g.wordApp.model.sec.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity(name = "auth_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authCodeId;
    private String authCode;
    private Timestamp createdAt;
    private Boolean wasItUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
