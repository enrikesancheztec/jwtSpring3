package com.example.jwt.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.jwt.config.GestionUsuariosConfig;
import com.example.jwt.entities.Usuario;
import com.example.jwt.repositories.UsuarioRepository;
import com.example.jwt.security.JPADetalleUsuariosService;
import com.example.jwt.security.JwtService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Optional;

@WebMvcTest(HolaController.class)
@Import({
    GestionUsuariosConfig.class,
    JwtService.class,
    JPADetalleUsuariosService.class
})
class HolaControllerTest {
    @Autowired private MockMvc mvc;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Test
    void testHelloSinToken() throws Exception {
        mvc.perform(get("/hello")
            .contentType(MediaType.ALL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized()
        );
    }

    @Test
    void testHello() throws Exception {
        Usuario usuarioJohn = new Usuario();
        usuarioJohn.setNombreUsuario("john");
        usuarioJohn.setAutoridad("read");
        Optional<Usuario> optionalUsuarioJohn = Optional.of(usuarioJohn);
        
        Mockito.when(usuarioRepository.findByNombreUsuario(anyString())).thenReturn(optionalUsuarioJohn);

        mvc.perform(get("/hello")
            .header("Authorization","Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb2huIiwiZXhwIjoiMjEyNC0xMS0yNFQyMTo1NDowNC42NzdaIiwiaWF0IjoiMjAyNC0xMS0yNFQyMTo1NDowNC42NzdaIn0.Ltxm7BkIKVtpzRHBqx4WcPo4CHpbCTlclOhFaEAgH9FPAM9pjnzGaxzaJk6W3JAu")
            .contentType(MediaType.ALL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(content().string("Hello!")
        );
    }

    @Test
    void testHola() throws Exception {
        Usuario usuarioJohn = new Usuario();
        usuarioJohn.setNombreUsuario("john");
        usuarioJohn.setAutoridad("read");
        Optional<Usuario> optionalUsuarioJohn = Optional.of(usuarioJohn);
        
        Mockito.when(usuarioRepository.findByNombreUsuario(anyString())).thenReturn(optionalUsuarioJohn);

        mvc.perform(get("/hola")
            .header("Authorization","Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb2huIiwiZXhwIjoiMjEyNC0xMS0yNFQyMTo1NDowNC42NzdaIiwiaWF0IjoiMjAyNC0xMS0yNFQyMTo1NDowNC42NzdaIn0.Ltxm7BkIKVtpzRHBqx4WcPo4CHpbCTlclOhFaEAgH9FPAM9pjnzGaxzaJk6W3JAu")
            .contentType(MediaType.ALL))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden()
        );
    }
}
