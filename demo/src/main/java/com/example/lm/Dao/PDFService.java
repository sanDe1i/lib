package com.example.lm.Dao;

import com.example.lm.Model.PDFs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PDFService extends JpaRepository<PDFs, Integer> {

}
