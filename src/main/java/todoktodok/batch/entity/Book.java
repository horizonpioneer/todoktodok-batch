package todoktodok.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String summary;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false, length = 13)
    private String isbn;

    private String image;

    @Builder
    public Book(String title, String summary, String author, String publisher, String isbn, String image) {
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.image = image;
    }
}