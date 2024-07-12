package com.example.lm.Dao;

import com.example.lm.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findByBookId(Integer bookId);

}
