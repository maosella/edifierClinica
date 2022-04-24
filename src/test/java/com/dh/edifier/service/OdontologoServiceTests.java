package com.dh.edifier.service;

import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.OdontologoDTO;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class OdontologoServiceTests {

    @Autowired
    private IOdontologoService odontologoService;
    private OdontologoDTO odontologo;

    @BeforeEach
    public void setUp(){
        odontologo = new OdontologoDTO();
        odontologo.setNombre("Martin");
        odontologo.setApellido("Osella");
        odontologo.setMatricula(156156);
    }

    @Test
    public void test01AgregarOdontologo() throws Exception{
        OdontologoDTO o = odontologoService.crear(odontologo);
        assertNotNull(odontologoService.buscarPorId(o.getId()));
    }

    @Test
    public void test02ActualizarOdontologo() throws Exception{
        OdontologoDTO o = odontologoService.crear(odontologo);
        OdontologoDTO original = odontologoService.buscarPorId(o.getId());
        o.setNombre("Pedro");
        OdontologoDTO actualizado = odontologoService.actualizar(o);
        assertNotEquals(actualizado, original);
    }

    @Test
    public void test03EliminarOdontologo() throws Exception{
        OdontologoDTO o = odontologoService.crear(odontologo);
        assertNotNull(odontologoService.buscarPorId(o.getId()));
        odontologoService.eliminar(o.getId());
        assertThrows(ResourceNotFoundException.class, () -> odontologoService.buscarPorId(o.getId()));
    }

    @Test
    public void test04ObtenerTodosLosOdontologos() throws Exception{
        odontologoService.crear(odontologo);
        assertNotEquals(0, odontologoService.consultarTodos().size());
    }
}
