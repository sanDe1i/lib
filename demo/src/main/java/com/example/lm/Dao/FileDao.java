package com.example.lm.Dao;

import com.example.lm.Model.File;
import com.example.lm.Model.FileInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

@Repository
public interface FileDao extends MongoRepository<File, String> {
    @Query(value = "{ 'resourcesId' : ?0 }", fields = "{ 'filename' : 1 }")
    List<File> findFilenamesByResourcesId(int resourcesId);

}
