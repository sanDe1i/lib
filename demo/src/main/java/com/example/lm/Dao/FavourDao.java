package com.example.lm.Dao;

import com.example.lm.Model.Favour;
import com.example.lm.Model.FileInfo;
import com.example.lm.Model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FavourDao extends JpaRepository<Favour, Long> {
    Set<Favour> findByUser(UserInfo user);
    boolean existsByUserAndBook(UserInfo user, FileInfo book);
    Optional<Favour> findByUserAndBook(UserInfo user, FileInfo book);
}
