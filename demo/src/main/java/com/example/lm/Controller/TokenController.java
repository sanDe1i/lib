package com.example.lm.Controller;

import com.example.lm.Service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("/info")
    @CrossOrigin(origins = "http://127.0.0.1:8080")
    public String getInfo(String name) {
        return "Successfully access!!" + " Hello, " + name;
    }

    @PostMapping("/info1")
    public String getInfo1(String name) {
        return "asasasa";
    }

    @PostMapping("/generate_temp_token")
    public String tempToken(@RequestParam("username") String username) {
        String token = tokenService.generateTempToken(username);
        System.out.println("Token granted:" + token);
        if (validateToken(token)){
            System.out.println("ok");
            return token;
        }else {
            return "Token is invalid";
        }

    }

    @PostMapping("/refresh_token")
    public String refreshToken(String token){
        return tokenService.refreshToken(token);
    }
    @PostMapping("/generate_long_token")
    public String longToken(@RequestParam(required = false,name = "days",defaultValue = "7") int days,
                            @RequestParam("token") String token) {
        String longTermToken = tokenService.generateLongTermToken(days,token);
        System.out.println("Long Token granted:" + longTermToken);
        return longTermToken;
    }

    @PostMapping("/decode")
    public String decodeToken(@RequestParam("token") String token){
        return tokenService.decodeUsername(token);
    }

    @PostMapping("/validate")
    public Boolean validateToken(@RequestParam("token") String token){
        return tokenService.validateToken(token);
    }

}
