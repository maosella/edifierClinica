package com.dh.edifier.service.impl.auth;

import com.dh.edifier.entities.auth.Rol;
import com.dh.edifier.entities.auth.Usuario;
import com.dh.edifier.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements ApplicationRunner {

    private final UsuarioService usuarioService;

    @Autowired
    public DataLoader(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    public void run(ApplicationArguments args) throws BadRequestException {

        usuarioService.crear(new Usuario(35865174, "admin", "admin@correo.com", "admin", Set.of(new Rol("ADMIN"), new Rol("USER"))));
        usuarioService.crear(new Usuario(34785145, "user", "user@correo.com", "user", Set.of(new Rol("USER"))));

    }
}
