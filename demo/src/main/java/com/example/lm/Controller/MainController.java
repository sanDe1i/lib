package com.example.lm.Controller;

import com.example.lm.Model.File;
import com.example.lm.Model.FileInfo;
import com.example.lm.Model.ResourcesLib;
import com.example.lm.Service.FileService;
import com.example.lm.Service.ResourcesLibService;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private FileService fileService;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private ResourcesLibService resourcesLibService;

    @GetMapping("test")
    public String upload() {
        return "upload";
    }

    @GetMapping("resources")
    public String resourcesLib() {
        return "ResourcesLib";
    }

    @GetMapping("/files")
    public String listFiles(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<GridFSFile> files = fileService.listFiles(keyword);
        model.addAttribute("files", files);
        return "lists";
    }

    @GetMapping("/resourcesLib")
    public String getFileExplorer(Model model) {
        List<ResourcesLib> folders = resourcesLibService.getAllPackages();
        Map<Integer, List<File>> folderPDFMap = new HashMap<>();
        for (ResourcesLib folder : folders) {
            List<File> PDFs = fileService.getPDFsByLib(folder.getId());
            folderPDFMap.put(folder.getId(), PDFs);
        }
        model.addAttribute("folders", folders);
        model.addAttribute("folderPDFMap", folderPDFMap);
        return "ResourcesLib";
    }


    @PostMapping("/add-folder")
    public String addFolder(@RequestParam String folderName) {
        resourcesLibService.addFolder(folderName);
        return "redirect:/resourcesLib";
    }

    @PostMapping("/rename-folder")
    public String renameFolder(@RequestParam("folderId") int folderId, @RequestParam String newName) {
        resourcesLibService.renameFolder(folderId, newName);
        return "redirect:/resourcesLib";
    }

    @PostMapping("/uploadMARC")
    public String uploadMarcFile(@RequestParam("folderId") int folderId, @RequestParam("file") MultipartFile marc) {
        fileService.uploadMARCFile(folderId, marc);
        return "redirect:/resourcesLib";
    }

    @PostMapping("/uploadPDF")
    public ResponseEntity<?> handlePDFUpload(@RequestParam int folderId, @RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> invalidFiles = fileService.savePDFs(folderId, files);
        Map<String, List<String>> map = new HashMap<>();
        map.put("invalid", invalidFiles);
        return ResponseEntity.ok(map);
    }


    // 处理文件下载请求
    @PostMapping("/download/{fileID}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileID") String fileID) throws IOException {
        GridFSFile file = fileService.getFileById(fileID);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        InputStream inputStream = gridFSBucket.openDownloadStream(file.getObjectId());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMetadata().getString("_contentType")))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new InputStreamResource(inputStream));
    }

    @PostMapping("/delete/{fileID}")
    public String deleteFile(@PathVariable("fileID") String fileID) {
        fileService.deleteFileById(fileID);
        return "redirect:/files";
    }

    @GetMapping("/marcDetails")
    public ResponseEntity<?> getMarcDetails(@RequestParam("folderId") int id) {
        List<FileInfo> marcDetails = fileService.getMarcDetailByID(id);

        return ResponseEntity.ok(marcDetails);
    }

    @GetMapping("/fileview/pdf/keyword/{keyword}")
    @ResponseBody
    public List<FileInfo> getPDFByKeyword(@PathVariable String keyword) {
        System.out.println(keyword);
        List<FileInfo> files = fileService.keywordSearch(keyword);
        if (files == null || files.isEmpty()) {
            System.out.println("No files found for the given keyword.");
        } else {
            for (FileInfo file : files) {
                System.out.println("Found file: " + file.getId());
            }
        }
        return files;
    }

//    @GetMapping("test/search")
//    public List<FileInfo> searchDocumentsByTitle(@RequestParam String title) {
//        return fileService.searchByTitle(title);
//    }

}
