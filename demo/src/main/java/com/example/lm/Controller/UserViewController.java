package com.example.lm.Controller;

import com.example.lm.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class UserViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/banUser")
    public String banUserPage() {
        return "banUser"; // 这里的"banUser"应该对应的是banUser.html
    }

    @PostMapping("/banUser")
    public ResponseEntity<?> banUser(@RequestBody Map<String, String> request) {
        int userId = Integer.parseInt(request.get("userId"));
        int banDuration = Integer.parseInt(request.get("banDuration"));
        System.out.println("userId: " + userId + ", banDuration: " + banDuration);
        userService.banUser(userId, banDuration);
        return ResponseEntity.ok("User banned successfully");
    }
}
