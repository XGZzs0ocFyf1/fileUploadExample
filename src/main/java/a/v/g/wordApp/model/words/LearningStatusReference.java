package a.v.g.wordApp.model.words;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_status_reference")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearningStatusReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "percentage", nullable = false, unique = true)
    private int percentage;

    @Column(name = "status_name", nullable = false)
    private String statusName;
}

