package com.example.lm.Dao;

import com.example.lm.Model.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoDao extends JpaRepository<FileInfo, Integer> {
    FileInfo save(FileInfo fi);

    @Query("SELECT f FROM FileInfo f WHERE f.isbn LIKE %:isbn%")
    List<FileInfo> findByIsbnContaining(@Param("isbn") String isbn);

    List<FileInfo> findByResourcesId(int resourcesId);

    FileInfo findByisbn(String ISBN);

 }
