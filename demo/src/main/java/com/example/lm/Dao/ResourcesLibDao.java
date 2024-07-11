package com.example.lm.Dao;

import com.example.lm.Model.ResourcesLib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcesLibDao extends JpaRepository<ResourcesLib, Integer> {
   ResourcesLib save(ResourcesLib rl);

   ResourcesLib findResourcesLibById(int id);
}
