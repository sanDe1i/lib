package com.example.lm.Controller;

import com.example.lm.Model.FileInfo;
import com.example.lm.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

        // 使用 Set 进行去重
        Set<Map<String, Object>> resultSet = fileInfos.stream()
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
                    return result;
                })
                .collect(Collectors.toSet());  // Collect into a Set to remove duplicates
        List<Map<String, Object>> resultList = new ArrayList<>(resultSet);
        int resultCount = resultList.size();
        // 将去重后的结果转换回 List
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


}
