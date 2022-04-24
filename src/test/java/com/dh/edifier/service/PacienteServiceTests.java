package com.dh.edifier.service;


import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.DomicilioDTO;
import com.dh.edifier.model.PacienteDTO;
import com.dh.edifier.service.impl.PacienteService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class PacienteServiceTests {

    @Autowired
    private PacienteService pacienteService;
    private PacienteDTO paciente;

    @BeforeEach
    public void setUp() {
        DomicilioDTO domicilio = new DomicilioDTO("Calle Falsa", 123, "Springfield", "Springfield");
        paciente = new PacienteDTO("pepe", "papardo", 15668778, domicilio);
    }

    @Test
    public void test01AgregarPaciente() throws Exception {
        PacienteDTO p = pacienteService.crear(paciente);
        assertNotNull(pacienteService.buscarPorId(p.getId()));
    }

    @Test
    public void test02ActualizarPaciente() throws Exception {
        PacienteDTO p = pacienteService.crear(paciente);
        PacienteDTO original = pacienteService.buscarPorId(p.getId());
        p.setNombre("Pepito");
        PacienteDTO actualizado = pacienteService.actualizar(p);
        assertNotEquals(actualizado, original);
    }

    @Test
    public void test03EliminarPaciente() throws Exception {
        PacienteDTO p = pacienteService.crear(paciente);
        assertNotNull(pacienteService.buscarPorId(p.getId()));
        pacienteService.eliminar(p.getId());
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.buscarPorId(p.getId()));
    }

    @Test
    public void test04ObtenerTodosLosPacientes() throws Exception {
        pacienteService.crear(paciente);
        assertNotEquals(0, pacienteService.consultarTodos().size());
    }
}

