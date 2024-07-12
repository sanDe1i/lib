package com.example.lm.Dao;

import com.example.lm.Model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRepository extends JpaRepository<Borrow, Integer> {
    Borrow findByBorrowId(Integer borrowId);

}
