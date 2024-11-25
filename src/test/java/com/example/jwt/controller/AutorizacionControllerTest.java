package com.example.jwt.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.jwt.config.GestionUsuariosConfig;
import com.example.jwt.entities.Usuario;
import com.example.jwt.repositories.UsuarioRepository;
import com.example.jwt.security.JPADetalleUsuariosService;
import com.example.jwt.security.JwtService;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@WebMvcTest(AutorizacionController.class)
@Import({
    GestionUsuariosConfig.class,
    JPADetalleUsuariosService.class,
    JwtService.class
})
class AutorizacionControllerTest {
    @Autowired private MockMvc mvc;

    @MockBean private UsuarioRepository usuarioRepository;

    @Test
    void testAutenticar() throws Exception {
        Usuario usuarioJohn = new Usuario();
        usuarioJohn.setNombreUsuario("john");
        usuarioJohn.setAutoridad("read");
        usuarioJohn.setContraseña("$2a$10$eSarrpDdKRjIZqsQTlvOAOfwqKnL8Gxzatn3vNid/hcGRTYEdRyu2");
        Optional<Usuario> optionalUsuarioJohn = Optional.of(usuarioJohn);

        Mockito.when(usuarioRepository.findByNombreUsuario(anyString())).thenReturn(optionalUsuarioJohn);

        mvc.perform(
                post("/autenticar")
                        .accept(MediaType.ALL)
                        .content("{\"usuario\": \"john\", \"contraseña\": \"12345\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAutenticarCredencialesInvalidas() throws Exception {
        Optional<Usuario> optionalUsuarioJohn = Optional.empty();

        Mockito.when(usuarioRepository.findByNombreUsuario(anyString())).thenReturn(optionalUsuarioJohn);

        mvc.perform(
                post("/autenticar")
                        .accept(MediaType.ALL)
                        .content("{\"usuario\": \"raul\", \"contraseña\": \"78945\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
