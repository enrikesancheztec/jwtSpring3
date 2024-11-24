package com.example.jwt.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Component
public class JwtService {
    private String secretoJwt = "c13994aec64de45c03dddf6490eebeb60f3b83c4437aa0611771c88ae12ca8e7";

    public String generarToken(String nombreUsuario) {
        Map<String, Object> concesiones = new HashMap<>();

        return Jwts.builder()
                .claims().add(concesiones).and()
                .subject(nombreUsuario)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(generarLlaveDigital())
                .compact();
    }

    SecretKey generarLlaveDigital() {
        byte[] llave = Decoders.BASE64.decode(secretoJwt);
        return Keys.hmacShaKeyFor(llave);
    }

    public String extraerNombreUsuario(String token) {
        return extraerConcesionEspecifica(token, Claims::getSubject);
    }

    public Date extraerExpiracion(String token) {
        return extraerConcesionEspecifica(token, Claims::getExpiration);
    }

    private <T> T extraerConcesionEspecifica(String token, Function<Claims, T> resolvedorConcesion) {
        final Claims concesiones = extraerConcesiones(token);
        return resolvedorConcesion.apply(concesiones);
    }

    private Claims extraerConcesiones(String token) {
        return Jwts.parser()
                .verifyWith(generarLlaveDigital())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean haExpiradoToken(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    public boolean validarToken(String token, UserDetails detallesUsuario) {
        final String nombreUsuario = extraerNombreUsuario(token);
        return (nombreUsuario.equals(detallesUsuario.getUsername()) && !haExpiradoToken(token));
    }
}
