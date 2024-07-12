package com.example.lm.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;
    private String title;
    private String alternativeTitle;
    private String sourceType;
    private String authors;
    private String editors;
    private String series;
    private String language;
    private String isbn;
    private String publisher;
    private String published;
    private String edition;
    private String copyrightYear;
    private String copyrightDeclaration;
    private String status;
    private String url;
    private int pages;
    private String subjects;
    private String description;
    private String chapters;
    private String bookDatabases;
    private String originalSource;
    private String contributingInstitution;
    private String digitizationExplanation;
    private String loanLabel;

}
