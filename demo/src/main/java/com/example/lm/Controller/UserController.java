package com.example.lm.Controller;

import com.example.lm.Model.FileInfo;
import com.example.lm.Model.UserInfo;
import com.example.lm.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

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

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) {
        boolean authenticated = userService.authenticate(username, password);
        if (authenticated) {
            model.addAttribute("message", "Login successful!");
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            return "redirect:/";
        } else {
            model.addAttribute("message", "Login failed!");
            return "redirect:/login.html"; // 如果登录失败，重新跳转到登录页面
        }
    }


    //    @PostMapping("/addBookToCollection")
//    public String addBookToCollection(@RequestParam String username, @RequestParam String bookId) {
//        try {
//            userService.addBookToUserCollection(username, bookId);
//            return "Book added to collection!";
//        } catch (IllegalArgumentException e) {
//            return e.getMessage();
//        }
//    }
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
    public ResponseEntity<?> loginUser(@RequestBody UserInfo user) {
        UserInfo user1 = userService.userLogin(user.getUsername());
        System.out.println(user1.getPassword());
        if (user1 != null) {
            if (user1.getPassword().equals(user.getPassword())) {
                System.out.println("Password is correct");
                // 返回一个包含 success 和 token 的 JSON 对象
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", Collections.singletonMap("token", "dummy-token"));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Password is incorrect", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/user/staff/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

        UserInfo user1 = userService.userLogin(username);
        System.out.println(user1.getPassword());
        if (user1 != null) {
            if (user1.getPassword().equals(password)) {
                System.out.println("Password is correct");
                // 返回一个包含 success 和 token 的 JSON 对象
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("data", Collections.singletonMap("token", "dummy-token"));
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Password is incorrect", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

}