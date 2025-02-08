package a.v.g.wordApp.model.words;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "word_translations", schema = "word_app")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WordTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_translations_id")
    private Long wordTranslationsId;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Column(name = "language_code", nullable = false)
    private String languageCode;

    @Column(name = "translation", nullable = false)
    private String translation;

    @Column(name = "example_sentence")
    private String exampleSentence;

    @Column(name = "image_url")
    private String imageUrl;
}
