package a.v.g.wordApp.model.words;



import a.v.g.wordApp.model.sec.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_status_id")
    private Long learningStatusId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private LearningStatusReference status;
}
