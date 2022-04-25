package com.dh.edifier.config;

import com.dh.edifier.service.impl.auth.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsuarioService usuarioService;

    @Autowired
    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, UsuarioService usuarioService){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.usuarioService = usuarioService;
    }

    public WebSecurityConfig(boolean disableDefaults, BCryptPasswordEncoder bCryptPasswordEncoder, UsuarioService usuarioService){
        super(disableDefaults);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/turnos/**").hasAuthority("USER")
                    .antMatchers("/odontologos/**", "/pacientes/**").hasAuthority("ADMIN")
                    .antMatchers("/index.html", "/registro-turno.html").hasAuthority("USER")

                    .antMatchers("/registro-odontologo.html",
                                            "/registro-paciente.html",
                                            "/registro-usuario.html",
                                            "/lista-odontologos.html",
                                            "/lista-pacientes.html",
                                            "/lista-usuarios.html").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .exceptionHandling().accessDeniedPage("/403");


    }
}
