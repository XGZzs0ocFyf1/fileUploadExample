package a.v.g.wordApp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "image_links")
@Data
public class ImageLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName; // Имя файла в S3
    private String url;      // Полная ссылка на файл
}
