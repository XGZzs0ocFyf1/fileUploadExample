package a.v.g.wordApp.model.words;




import a.v.g.wordApp.model.sec.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_words")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_words_id")
    private Long userWordsId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;
}
