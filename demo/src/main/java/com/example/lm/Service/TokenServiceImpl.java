package com.example.lm.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenServiceImpl implements TokenService{


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder encoder;

    @Autowired
    private JwtDecoder decoder;



    public String generateTempToken(String username) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(username)
                .claim("type", "temp")
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }


    public String refreshToken(String token) {
        System.out.println("refresh");
        if (validateToken(token)){
            if(decodeType(token).equals("long")){
                String refreshToken = generateTempToken(decodeUsername(token));
                System.out.println("refresh short: " + refreshToken);
                return refreshToken;
            }
        }
            return "Invalid token";
    }


    public String generateLongTermToken(int days, String token){
        System.out.println("token received: "+token);
        if (validateToken(token)){
            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .subject(decodeUsername(token))
                    .claim("type", "long")
                    .build();
            return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        }else {
            return "Invalid token";
        }


    }

    public String decodeType(String token) {
        if (validateToken(token)){
            Jwt jwt = this.decoder.decode(token);
            String claim = jwt.getClaim("type");
            System.out.println("type: "+claim);
            return claim;
        }else {
            return "Invalid token";
        }

    }


    public String decodeUsername(String token) {
        if (validateToken(token)){
            Jwt jwt = this.decoder.decode(token);
            String subject = jwt.getSubject();
            System.out.println(subject);
            return subject;
        }else {
            return "Invalid";
        }

    }

    public String decodePassword(String token) {
        if (validateToken(token)){
            Jwt jwt = this.decoder.decode(token);
            String claim = jwt.getClaim("password");
            System.out.println(claim);
            return claim;
        }else {
            return "Invalid token";
        }

    }

    public Boolean validateToken(String token){
        try {
            Jwt jwt = this.decoder.decode(token);
            Instant now = Instant.now();
            Instant expiration = jwt.getExpiresAt();
            assert expiration != null;
            System.out.println(expiration.toString());
            if (now.isAfter(expiration)){
                System.out.println("expire");
            }
            return !now.isAfter(expiration);
        } catch (Exception e) {
            System.out.println("exception");
            return false;
        }
    }

    public String loginUser(String token, HttpServletRequest request){
        if (validateToken(token)){
            try {
                String username = decodeUsername(token);
                String password = decodePassword(token);
                UsernamePasswordAuthenticationToken authReq =
                        new UsernamePasswordAuthenticationToken(username, password);
                Authentication auth = authenticationManager.authenticate(authReq);
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
                return "fine";
            } catch (AuthenticationException e) {
                return "error login";
            }

        }else {
            return "invalid token";
        }
    }
}

