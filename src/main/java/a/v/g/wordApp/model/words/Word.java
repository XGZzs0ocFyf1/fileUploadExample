package a.v.g.wordApp.model.words;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Table(name = "word", schema = "word_app")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "words_id")
    private Long wordsId;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "transcription")
    private String transcription;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "word", cascade = CascadeType.ALL)
    private List<WordTranslation> translations;
}
