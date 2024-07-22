package com.example.lm.Controller;

import com.example.lm.Dao.PDFDao;
import com.example.lm.Model.FileInfo;
import com.example.lm.Model.ResourcesLib;
import com.example.lm.Service.FileService;
import com.example.lm.Service.ResourcesLibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ListBookController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ResourcesLibService resourcesLibService;

    @Autowired
    private PDFDao pdfRepository;

    @Value("${PDFUploadPath}")
    private String PDFUploadPath;

    @GetMapping("test/search2")
    public String searchBooks(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String publisher,
                              @RequestParam(required = false) String sourceType,
                              @RequestParam(required = false) String language,
                              @RequestParam(required = false) String published,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) Integer databaseId,
                              Model model) {
        List<FileInfo> fileInfos = fileService.searchBooks(title, status, publisher, sourceType, language, published, databaseId);

        // Collect all resource IDs
        Set<Integer> resourceIds = fileInfos.stream()
                .map(FileInfo::getResourcesId)
                .collect(Collectors.toSet());

        // Fetch all ResourcesLib objects in one batch
        Map<Integer, ResourcesLib> resourcesLibMap = resourcesLibService.findResourcesLibByIds(resourceIds);

        List<Map<String, Object>> resultSet = fileInfos.stream()
                .map(fileInfo -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("title", fileInfo.getTitle());
                    result.put("alternativeTitle", fileInfo.getAlternativeTitle());
                    result.put("sourceType", fileInfo.getSourceType());
                    result.put("author", fileInfo.getAuthors());
                    result.put("isbn", fileInfo.getIsbn());
                    result.put("publisher", fileInfo.getPublisher());
                    result.put("published", fileInfo.getPublished());
                    result.put("status", fileInfo.getStatus());
                    result.put("view", fileInfo.getView());
                    result.put("download", fileInfo.getDownload());
                    result.put("downloadLink", fileInfo.getDownloadLink());

                    // Get ResourcesLib object from the map
                    ResourcesLib resourcesLib = resourcesLibMap.get(fileInfo.getResourcesId());
                    String resourcesLibName = (resourcesLib != null) ? resourcesLib.getName() : "";
                    result.put("resourcesId", resourcesLibName);

                    result.put("loanLabel", fileInfo.getLoanLabel());
                    int id = fileInfo.getId();
                    result.put("loaned", fileService.isBookBorrowed(id));
                    result.put("id", id);
                    return result;
                }).toList();  // Collect into a Set to remove duplicates

        List<Map<String, Object>> resultList = new ArrayList<>(resultSet);
        int resultCount = resultList.size();
        model.addAttribute("books", new ArrayList<>(resultSet));
        model.addAttribute("resultCount", resultCount);

        // Pass search parameters to the view
        model.addAttribute("title", title);
        model.addAttribute("publisher", publisher);
        model.addAttribute("sourceType", sourceType);
        model.addAttribute("language", language);
        model.addAttribute("published", published);
        model.addAttribute("status", status);
        model.addAttribute("databaseId", databaseId);

        // Fetch distinct values once
        List<String> publishers = fileService.getAllDistinctPublishers();
        List<String> publisheds = fileService.getAllDistinctPublished();
        List<String> sourceTypes = fileService.getAllDistinctSourceType();
        List<String> languages = fileService.getAllDistinctLanguage();
        List<String> statuses = fileService.getAllDistinctStatus();
        List<Integer> databaseIds = fileService.getAllDistinctDatabaseId();
        Map<Integer, String> databaseInfoMap = databaseIds.stream()
                .collect(Collectors.toMap(id -> id, id -> {
                    ResourcesLib resourcesLib = resourcesLibService.findResourcesLibById(id);
                    return (resourcesLib != null) ? resourcesLib.getName() : "Unknown Database";
                }));

        model.addAttribute("databaseInfoMap", databaseInfoMap);
        model.addAttribute("publishers", publishers);
        model.addAttribute("publisheds", publisheds);
        model.addAttribute("sourceTypes", sourceTypes);
        model.addAttribute("languages", languages);
        model.addAttribute("statuses", statuses);
        model.addAttribute("databaseMap", databaseInfoMap);

        return "searchresults";  // Return view name
    }

    @PostMapping("test/deleteList/{fileID}")
    public String deleteFile(@PathVariable("fileID") int fileID,
                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) String publisher,
                             @RequestParam(required = false) String sourceType,
                             @RequestParam(required = false) String language,
                             @RequestParam(required = false) String published,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false) Integer databaseId) {
        fileService.deleteBook(fileID);
        String redirectUrl = String.format("/test/search2?title=%s&publisher=%s&sourceType=%s&language=%s&published=%s&status=%s&databaseId=%s",
                encodeParam(title), encodeParam(publisher), encodeParam(sourceType), encodeParam(language), encodeParam(published), encodeParam(status), encodeParam(databaseId));

        return "redirect:" + redirectUrl;

    }

    @GetMapping("test/book/{id}")
    public String getBookDetails(@PathVariable Integer id, Model model,
                                 @RequestParam(required = false) String title,
                                 @RequestParam(required = false) String publisher,
                                 @RequestParam(required = false) String sourceType,
                                 @RequestParam(required = false) String language,
                                 @RequestParam(required = false) String published,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) Integer databaseId) {
        FileInfo fileInfo = fileService.getBookById(id);
        if (fileInfo != null) {
            model.addAttribute("title", title);
            model.addAttribute("publisher", publisher);
            model.addAttribute("sourceType", sourceType);
            model.addAttribute("language", language);
            model.addAttribute("published", published);
            model.addAttribute("status", status);
            model.addAttribute("databaseId", databaseId);
            model.addAttribute("book", fileInfo);

            return "viewDetails";  // 返回书籍详细信息的视图名称
        } else {
            return "redirect:/test/searchresults";  // 如果找不到书籍，重定向回搜索结果页面
        }
    }

    @PostMapping("/book/update")
    public String updateBook(@ModelAttribute FileInfo fileInfo,
                             @RequestParam String titleSearched,
                             @RequestParam String publisherSearched,
                             @RequestParam String sourceTypeSearched,
                             @RequestParam String languageSearched,
                             @RequestParam String publishedSearched,
                             @RequestParam String statusSearched,
                             @RequestParam String databaseIdSearched) {
        fileService.updateBook(fileInfo);

        String redirectUrl = String.format("redirect:/test/search2?title=%s&publisher=%s&sourceType=%s&language=%s&published=%s&status=%s&databaseId=%s",
                titleSearched, publisherSearched, sourceTypeSearched, languageSearched, publishedSearched, statusSearched, databaseIdSearched);
        return redirectUrl;
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable int id, @RequestParam String newStatus) {
        fileService.updateStatus(id, newStatus);
        return ResponseEntity.ok("Status updated successfully");
    }

    @PutMapping("/updateLoan/{id}")
    public ResponseEntity<String> updateLoan(@PathVariable int id, @RequestParam String newLoan) {
        fileService.updateLoan(id, newLoan);
        return ResponseEntity.ok("Loan updated successfully");
    }

    @PostMapping("test/updateField")
    @ResponseBody
    public ResponseEntity<String> updateField(@RequestParam("id") int id,
                                              @RequestParam("editType") String editType,
                                              @RequestParam("editField") String editField) {
        try {
            fileService.updateField(id, editType, editField);
            return ResponseEntity.ok("Field updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating field");
        }
    }

    @GetMapping("/isBorrowed/{bookId}")
    public ResponseEntity<Boolean> isBookBorrowed(@PathVariable Integer bookId) {
        boolean isBorrowed = fileService.isBookBorrowed(bookId);
        return ResponseEntity.ok(isBorrowed);
    }

    private String encodeParam(Object param) {
        try {
            return param == null ? "" : URLEncoder.encode(param.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/downloadFromList/{bookId}")
    public ResponseEntity<Resource> downloadFile2(@PathVariable int bookId) {
        FileInfo f = fileService.getBookById(bookId);

        // Use regex to split by spaces and parentheses, removing non-digit parts
        String[] parts = f.getIsbn().replaceAll("[^\\d\\s]", "").split("\\s+");

        List<String> resultList = new ArrayList<>(Arrays.asList(parts));
        for (String part : parts) {
            resultList.add(part + ".pdf");
        }

        for (String fileName : resultList) {
            if (pdfRepository.existsByName(fileName)) {
                System.out.println("Downloading file with name: " + fileName);
                try {
                    Path filePath = Paths.get(PDFUploadPath).resolve(fileName).normalize();
                    Resource resource = new UrlResource(filePath.toUri());
//                    System.out.println(filePath);

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

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/files-view")
    public String fileList() {
        return "fileList";  // 这里返回的是视图名称，不包括.html扩展名
    }


}
