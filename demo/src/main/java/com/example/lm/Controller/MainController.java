package com.example.lm.Controller;

import com.example.lm.Model.File;
import com.example.lm.Model.FileInfo;
import com.example.lm.Model.ResourcesLib;
import com.example.lm.Service.FileService;
import com.example.lm.Service.ResourcesLibService;

import com.example.lm.utils.SearchResult;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private final Path fileStorageLocation = Paths.get("pdf").toAbsolutePath().normalize();

    @GetMapping("test")
    public String upload() {
        return "upload";
    }

    @GetMapping("/table")
    public ModelAndView showTable(@RequestParam String folderId) {
        ModelAndView modelAndView = new ModelAndView("table");
        modelAndView.addObject("folderId", folderId);
        return modelAndView;
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
        return "sourceDatabases";
    }


    @PostMapping("/addDatabases")
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
    public String handlePDFUpload(@RequestParam int folderId, @RequestParam("files") List<MultipartFile> files, RedirectAttributes redirectAttributes) throws IOException {
        List<String> invalidFiles = fileService.savePDFs(folderId, files);
        if (!invalidFiles.isEmpty()) {
            redirectAttributes.addFlashAttribute("invalidFiles", invalidFiles);
            redirectAttributes.addFlashAttribute("folderIdWithInvalidFiles", folderId);
        }
        return "redirect:/resourcesLib";
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

    @GetMapping("/keyword/{keyword}")
    @ResponseBody
    public SearchResult<FileInfo> getPDFByKeyword(@PathVariable String keyword,
                                                  @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page-1, 5);
        Page<FileInfo> files = fileService.keywordSearch(keyword, pageable);
        System.out.println(page);
        if (files.isEmpty()) {
            System.out.println("No files found for the given keyword.");
        } else {
            files.forEach(file -> System.out.println("Found file: " + file.getId()));
        }

        return new SearchResult<>(files.getContent(), files.getTotalElements());
    }

    @GetMapping("/keyword/test/{keyword}")
    @ResponseBody
    public SearchResult<FileInfo> searchFiles(
            @PathVariable String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String originalSource,
            @RequestParam(required = false) String language) {
        if ("undefined".equals(originalSource)) {
            originalSource = null;
        }
        if ("undefined".equals(language)) {
            language = null;
        }

        System.out.println("page: " + page);

        Pageable pageable = PageRequest.of(page-1, 5);
        Page<FileInfo> files = fileService.keywordSearch(keyword, originalSource, language, pageable);
        System.out.println(page);
        if (files.isEmpty()) {
            System.out.println("No files found for the given keyword.");
        } else {
            files.forEach(file -> System.out.println("Found file: " + file.getId()));
        }

        return new SearchResult<>(files.getContent(), files.getTotalElements());
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<FileInfo> getBookById(@PathVariable Integer id) {
        FileInfo fileInfo = fileService.getBookById(id);
        if (fileInfo != null) {
            return ResponseEntity.ok(fileInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/saveTable")
    public ResponseEntity<String> saveTable(@RequestBody List<Map<String, String>> tableData) {
        try {
            fileService.saveMarcDetails(tableData);
            return ResponseEntity.ok("保存成功！");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("保存失败！");
        }
    }

    @GetMapping("/downloadfiles/{fileId}")
    public ResponseEntity<Resource> downloadFileMysql(@PathVariable String fileId) {
        System.out.println("Downloading file with id: " + fileId);
        try {
            Path filePath = this.fileStorageLocation.resolve(fileId + ".pdf").normalize();
            System.out.println(filePath.toString());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
