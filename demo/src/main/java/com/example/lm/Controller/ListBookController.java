package com.example.lm.Controller;

import com.example.lm.Model.FileInfo;
import com.example.lm.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Controller
public class ListBookController {

    @Autowired
    private FileService fileService;

    // 显示查询页面
    @GetMapping("/test/searchPage")
    public String showSearchPage() {
        return "search2";  // 返回视图名称 (searchPage.html)
    }

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
                    result.put("resourcesId", fileInfo.getResourcesId());
                    result.put("loanLabel", fileInfo.getLoanLabel());
                    result.put("loaned", fileService.isBookBorrowed(fileInfo.getId()));
                    result.put("url", fileInfo.getUrl());
                    result.put("id", fileInfo.getId());
                    return result;
                }).toList();  // Collect into a Set to remove duplicates
        List<Map<String, Object>> resultList = new ArrayList<>(resultSet);
        int resultCount = resultList.size();
        model.addAttribute("books", new ArrayList<>(resultSet));
        model.addAttribute("resultCount", resultCount);
        // 同时将搜索参数传递给视图
        model.addAttribute("title", title);
        model.addAttribute("publisher", publisher);
        model.addAttribute("sourceType", sourceType);
        model.addAttribute("language", language);
        model.addAttribute("published", published);
        model.addAttribute("status", status);
        model.addAttribute("databaseId", databaseId);
        // 将出版社列表传递到视图
        List<String> publishers = fileService.getAllDistinctPublishers();
        List<String> publisheds = fileService.getAllDistinctPublished();
        List<String> SourceTypes = fileService.getAllDistinctSourceType();
        List<String> languages = fileService.getAllDistinctLanguage();
        List<String> statuses =fileService.getAllDistinctStatus();
        List<Integer> databaseIds = fileService.getAllDistinctDatabaseId();
        model.addAttribute("publishers", publishers);
        model.addAttribute("publisheds", publisheds);
        model.addAttribute("SourceTypes", SourceTypes);
        model.addAttribute("languages", languages);
        model.addAttribute("statuses", statuses);
        model.addAttribute("databaseIds", databaseIds);

        return "searchresults";  // 返回视图名称
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

//    @PostMapping("/book/update")
//    public String updateBook(@ModelAttribute FileInfo fileInfo) {
//        fileService.updateBook(fileInfo);
//
////        return "redirect:" + redirectUrl;
//        return "redirect:/book/" + fileInfo.getId();
//    }

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




}
