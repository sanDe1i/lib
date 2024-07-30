package com.example.lm.Service;

import com.example.lm.Dao.ResourcesLibDao;
import com.example.lm.Model.ResourcesLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResourcesLibService {
    @Autowired
    private ResourcesLibDao resourcesLibDao;

    /**
     *
     * @param name
     * @param type
     * @param title_en
     * @param description_en
     * @param title_cn
     * @param description_cn
     * displayCode: 0: Hidden; 1: Published
     *
     */
    /*public void createResourcesLib(String name, String type, String title_en,
                                   String description_en, String title_cn,
                                   String description_cn) {
        int hidden = 0;
        ResourcesLib rl = new ResourcesLib();
        rl.setName(name);
        rl.setType(type);
//        rl.setTitle_en(title_en);
//        rl.setTitle_cn(title_cn);
//        rl.setDescription_cn(description_cn);
        rl.setDescription_en(description_en);
        rl.setDisplay(hidden);
        resourcesLibDao.save(rl);
    }*/

    public List<ResourcesLib> getAllPackages() {
        return resourcesLibDao.findAll();
    }

    public void addFolder(String folderName) {
        ResourcesLib folder = new ResourcesLib();
        folder.setName(folderName);
        resourcesLibDao.save(folder);
    }

    public void renameFolder(int folderId, String newName) {
        ResourcesLib rl = resourcesLibDao.findResourcesLibById(folderId);
        rl.setName(newName);
        resourcesLibDao.save(rl);
    }

    public void saveNewDatabases(ResourcesLib rl) {
        resourcesLibDao.save(rl);
    }

    public ResourcesLib findResourcesLibById(Integer id){
        return resourcesLibDao.findResourcesLibById(id);
    }

    public Map<Integer, ResourcesLib> findResourcesLibByIds(Set<Integer> ids) {
        List<ResourcesLib> resourcesLibs = resourcesLibDao.findByIdIn(ids);
        return resourcesLibs.stream().collect(Collectors.toMap(ResourcesLib::getId, Function.identity()));
    }

    public void updateFolderDetails(int folderId, String newName, String newAlternateNames, String newType, String newDescription) {
        ResourcesLib folder = resourcesLibDao.findById(folderId).orElseThrow(() -> new IllegalArgumentException("Invalid folder ID"));
        folder.setName(newName);
        folder.setAlternateName(newAlternateNames);
        folder.setType(newType);
        folder.setDescription(newDescription);
        resourcesLibDao.save(folder);
    }
}
