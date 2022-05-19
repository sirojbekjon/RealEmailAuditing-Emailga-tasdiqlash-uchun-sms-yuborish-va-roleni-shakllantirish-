package com.example.appjwtrealemailauditing.security;

import com.example.appjwtrealemailauditing.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private static final long expireTime = 1000*60*60*24;
    private static final String secretkey ="MAXFIYSOZHECHKIMBILMASIN";

    public String generateToken(String  username, Set<Role> roles){
        Date date = new Date(System.currentTimeMillis() + expireTime);
        String token =
                Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .claim("roles",roles)
                .signWith(SignatureAlgorithm.HS512,secretkey)
                .compact();
        return token;

    }

    public String getEmailFromToken(String token){
        try{
            String email = Jwts
                    .parser()
                    .setSigningKey(secretkey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return email;

        }catch (Exception e){
            return null;
        }
    }
}
