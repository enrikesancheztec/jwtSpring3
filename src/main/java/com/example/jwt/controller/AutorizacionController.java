package com.example.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.security.JPADetalleUsuariosService;
import com.example.jwt.security.JwtService;
import com.example.jwt.vo.CredencialesVO;

@RestController
public class AutorizacionController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JPADetalleUsuariosService detalleUsuariosService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/autenticar")
    public String autenticar(@RequestBody CredencialesVO credenciales) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credenciales.getUsuario(), credenciales.getContraseña()));
        final UserDetails detalleUsuario = detalleUsuariosService.loadUserByUsername(credenciales.getUsuario());
        return jwtService.generarToken(detalleUsuario.getUsername());
    }
}
