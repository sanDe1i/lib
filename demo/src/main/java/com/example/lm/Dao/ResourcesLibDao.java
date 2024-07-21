package com.example.lm.Dao;

import com.example.lm.Model.ResourcesLib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ResourcesLibDao extends JpaRepository<ResourcesLib, Integer> {
   ResourcesLib save(ResourcesLib rl);

   ResourcesLib findResourcesLibById(Integer id);

   List<ResourcesLib> findByIdIn(Set<Integer> ids);
}
