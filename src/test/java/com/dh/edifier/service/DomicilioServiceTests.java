package com.dh.edifier.service;

import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.DomicilioDTO;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class DomicilioServiceTests {

    @Autowired
    private IDomicilioService domicilioService;
    private DomicilioDTO domicilio;

    @BeforeEach
    public void setUp() {
        domicilio = new DomicilioDTO();
        domicilio.setCalle("Calle Falsa");
        domicilio.setNumero(123);
        domicilio.setLocalidad("Almafuerte");
        domicilio.setProvincia("Cordoba");
    }

    @Test
    public void test01AgregarDomicilio() throws Exception{
        DomicilioDTO d = domicilioService.crear(domicilio);
        assertNotNull(domicilioService.buscarPorId(d.getId()));
    }

    @Test
    public void  test02ActualizarDomicilio() throws Exception{
        DomicilioDTO d = domicilioService.crear(domicilio);
        DomicilioDTO original = domicilioService.buscarPorId(d.getId());
        d.setCalle("Salvador");
        DomicilioDTO actualizado = domicilioService.actualizar(d);
        assertNotEquals(actualizado, original);
    }

    @Test
    public void test03EliminarDomicilio() throws Exception{
        DomicilioDTO d = domicilioService.crear(domicilio);
        assertNotNull(domicilioService.buscarPorId(d.getId()));
        domicilioService.eliminar(d.getId());
        assertThrows(ResourceNotFoundException.class, () -> domicilioService.buscarPorId(d.getId()));
    }

    @Test
    public void test04ObtenerTodosLosDomicilios() throws Exception{
        domicilioService.crear(domicilio);
        assertNotEquals(0, domicilioService.consultarTodos().size());
    }

}
