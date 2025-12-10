package com.unicauca.pensionados.backend.infrastructure.security.jwt;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.backend.domain.model.entity.Usuario;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

@Service
public class JwtService {
    
    private static final String SECRET_KEY = "u2Fh89dh2kLm19a0pPlA1hZx92kZ8tQ8Tf0mZsP4u9s=";

    public String getToken(UserDetails usuario){
        return getToken(new HashMap<>(),usuario);
    }

    private String getToken(Map<String,Object> extraClaims, UserDetails usuario){
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(usuario.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1 día
            .signWith(getKey(),SignatureAlgorithm.HS256)
            .compact();

    }

    public String getToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rolId", usuario.getRol().getId());   // 👈 Añades el idRol
        claims.put("rolNombre", usuario.getRol().getNombre()); // opcional
        return getToken(claims, usuario);
    }


    private Key getKey() {
        byte[] keyBytes=Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) 
    {
        final String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
        
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token,Claims::getSubject);
    }

    private Claims getAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver)
    {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

     private Date getExpiration(String token)
     { 
        return getClaim(token,Claims::getExpiration); 
     }

     private boolean isTokenExpired(String token)
     {
        return getExpiration(token).before(new Date());
     }
}
