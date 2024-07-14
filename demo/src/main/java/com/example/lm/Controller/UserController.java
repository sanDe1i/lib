package com.example.lm.Controller;

import com.example.lm.Model.FileInfo;
import com.example.lm.Model.User;
import com.example.lm.Model.UserInfo;
import com.example.lm.Service.TokenService;
import com.example.lm.Service.UserService;
import com.example.lm.utils.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // 检查会话中是否有用户名
        String username = (String) session.getAttribute("username");
        if (username == null) {
            // 如果用户名不存在，跳转到登录页面
            return "redirect:/log_in";
        }

        // 如果用户名存在，将其添加到模型
        model.addAttribute("username", username);

        String message = (String) model.asMap().get("message");
        if (message != null) {
            model.addAttribute("message", message);
        }

        return "home";
    }

    @GetMapping("/log_in")
    public String index(Model model) {
        String message = (String) model.asMap().get("message");
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "login";
    }


    @PostMapping("/addBookToCollection")
    public String addBookToCollection(HttpSession session, @RequestParam String bookisbn, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            // 如果用户名不存在，跳转到登录页面
            return "redirect:/log_in";
        }
        try {
            userService.addBookToUserCollection(username, bookisbn);
            model.addAttribute("message", "Add collection successful!");
            return "lists";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "lists";
        }
    }

    @PostMapping("/listCollection")
    public String listCollection(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/log_in";
        }
        Set<FileInfo> books = userService.listCollect(username);
        model.addAttribute("Collections", books);
        return "lists";
    }

    @PostMapping("/removeCollection")
    public String removeCollection(HttpSession session, Model model, @RequestParam String bookisbn) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/log_in";
        }
        try {
            userService.removeBookCollection(username, bookisbn);
            model.addAttribute("message", "Remove collection successful!");
            return "lists";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "lists";
        }
    }

    @PostMapping("/api/user/login")
    public Result login(@RequestBody String json) {
        System.out.println("Received JSON: " + json);

        // 解析 JSON
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = null;
        try {
            map = mapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 获取用户名和密码
        String username = map.get("username");
        String password = map.get("password");

        // 验证用户名和密码
        User user = userService.authenticate(username);
        if (user != null) {
            if (user.getPassword().equals(password)){

                Map<String,String> message = new HashMap<>();
                String token = tokenService.generateTempToken(String.valueOf(user.getId()));
                System.out.println("success token: " + token);
                message.put("token", token);
                message.put("id", String.valueOf(user.getId()));
                message.put("name", user.getUsername());
                return Result.ok().data(message);
            }else {
                return Result.error().data("error", "password error");
            }
        }else {
            return Result.error().data("error", "user not found");
        }
    }

}