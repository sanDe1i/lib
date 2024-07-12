package com.example.lm.Model;

import jakarta.persistence.*;


@Entity
@Table(name = "favour")
public class Favour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userInfo", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name = "fileInfo", nullable = false)
    private FileInfo book;

//    @ManyToOne
//    @JoinColumn(name = "collection", nullable = false)
//    private CollectIn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public FileInfo getBook() {
        return book;
    }

    public void setBook(FileInfo book) {
        this.book = book;
    }
}

