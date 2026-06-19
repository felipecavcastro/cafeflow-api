package com.cafeflow.service; // 1. O pacote precisa ser este!

import com.cafeflow.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey; // Import correto do Java
import java.util.Date;

@Service
public class JwtService {

    // Geramos uma chave secreta do tipo correto (SecretKey)
    private final SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    private final long expirationTime = 86400000;

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    // 2. O MÉTODO QUE O FILTRO ESTÁ PROCURANDO:
    public SecretKey getKey() {
        return this.key;
    }
}
