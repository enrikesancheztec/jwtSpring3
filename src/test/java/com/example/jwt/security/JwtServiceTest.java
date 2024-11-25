package com.example.jwt.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtServiceTest {
    JwtService jwtService = new JwtService();

    @BeforeEach
    public void prepararPruebas() {
        ReflectionTestUtils.setField(jwtService, "secretoJwt",
                "67889fd3269178d2526c57eb6357556127c8de46531b58720989563cf243a843");
    }

    @Test
    void testExtraerNombreUsuario() {
        // GIVEN
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb24iLCJleHAiOjQ4ODgxNDQ3NzYsImlhdCI6MTczMjQ3MTE3Nn0.yEx4sAvAz5KlDvENWg53jiLyfbeU1qtwXoS8nKd9Ytbs5YdzOdxsuNNjFdEhxaou";

        // WHEN
        String nombreUsuarioObtenido = jwtService.extraerNombreUsuario(token);

        // THEN
        String nombreUsuarioEsperado = "jon";
        assertEquals(nombreUsuarioEsperado, nombreUsuarioObtenido);
    }

    @Test
    void testExtraerExpiracion() {
        // GIVEN
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb24iLCJleHAiOjQ4ODgxNDQ3NzYsImlhdCI6MTczMjQ3MTE3Nn0.yEx4sAvAz5KlDvENWg53jiLyfbeU1qtwXoS8nKd9Ytbs5YdzOdxsuNNjFdEhxaou";

        // WHEN
        Date fechaExpiracionObtenida = jwtService.extraerExpiracion(token);

        // THEN
        String fechaCadena = "2124-11-24T11:59:36";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime fechaLocal = LocalDateTime.parse(fechaCadena, formateador);
        Date fechaExpiracionEsperada = Date.from(fechaLocal.atZone(ZoneId.systemDefault()).toInstant());
        assertEquals(fechaExpiracionEsperada, fechaExpiracionObtenida);
    }

    @Test
    void testGenerarToken() {
        // GIVEN
        String nombreUsuario = "jon";

        // WHEN
        String tokenObtenido = jwtService.generarToken(nombreUsuario);
        String nombreUsuarioObtenido = jwtService.extraerNombreUsuario(tokenObtenido);
        boolean expiradoObtenido = jwtService.haExpiradoToken(tokenObtenido);

        // THEN
        String nombreUsuarioEsperado = "jon";
        boolean expiradoEsperado = false;
        assertEquals(nombreUsuarioEsperado, nombreUsuarioObtenido);
        assertEquals(expiradoEsperado, expiradoObtenido);
    }

    @Test
    void testHaExpiradoToken() {
        // GIVEN
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb24iLCJleHAiOjQ4ODgxNDQ3NzYsImlhdCI6MTczMjQ3MTE3Nn0.yEx4sAvAz5KlDvENWg53jiLyfbeU1qtwXoS8nKd9Ytbs5YdzOdxsuNNjFdEhxaou";

        // WHEN
        boolean expiradoObtenido = jwtService.haExpiradoToken(token);

        // THEN
        boolean expiradoEsperado = false;
        assertEquals(expiradoEsperado, expiradoObtenido);
    }

    @Test
    void testValidarTokenUsuarioValido() {
        // GIVEN
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb24iLCJleHAiOjQ4ODgxNDQ3NzYsImlhdCI6MTczMjQ3MTE3Nn0.yEx4sAvAz5KlDvENWg53jiLyfbeU1qtwXoS8nKd9Ytbs5YdzOdxsuNNjFdEhxaou";
        UserDetails detallesUsuario = User.withUsername("jon")
                .password("12345")
                .authorities("lectura")
                .build();

        // WHEN
        boolean validacionObtenida = jwtService.validarToken(token, detallesUsuario);

        // THEN
        boolean validacionEsperada = true;
        assertEquals(validacionEsperada, validacionObtenida);
    }

    @Test
    void testValidarTokenUsuarioInvalido() {
        // GIVEN
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb24iLCJleHAiOjQ4ODgxNDQ3NzYsImlhdCI6MTczMjQ3MTE3Nn0.yEx4sAvAz5KlDvENWg53jiLyfbeU1qtwXoS8nKd9Ytbs5YdzOdxsuNNjFdEhxaou";
        UserDetails detallesUsuario = User.withUsername("pedro")
                .password("12345")
                .authorities("lectura")
                .build();

        // WHEN
        boolean validacionObtenida = jwtService.validarToken(token, detallesUsuario);

        // THEN
        boolean validacionEsperada = false;
        assertEquals(validacionEsperada, validacionObtenida);
    }
}
