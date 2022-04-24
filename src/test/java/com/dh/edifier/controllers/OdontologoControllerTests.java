package com.dh.edifier.controllers;

import com.dh.edifier.controller.impl.OdontologoController;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.GlobalExceptionsHandler;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.OdontologoDTO;
import com.dh.edifier.service.IOdontologoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utils.Mapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(OdontologoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OdontologoControllerTests {

    private MockMvc mockMvc;

    @Mock
    private IOdontologoService odontologoService;

    @InjectMocks
    private OdontologoController odontologoController;

    private OdontologoDTO odontologo;
    private OdontologoDTO odontologoInexistente;
    private OdontologoDTO odontologoExistente;

    @Before
    public void reset() throws BadRequestException, ResourceNotFoundException {
        mockMvc = MockMvcBuilders.standaloneSetup(odontologoController).setControllerAdvice(GlobalExceptionsHandler.class).build();
        odontologo = new OdontologoDTO(123456789, "Pepe", "Pepin", 123456);
        odontologoInexistente = new OdontologoDTO(123456789, "Pepe", "Pepin", 123456);
        odontologoExistente = new OdontologoDTO(123456789, "Pepe", "Pepin", 123456);
        odontologoExistente.setId(1);
        odontologoExistente.setId(1);
        odontologoInexistente.setId(999);
        configureMockito();
    }

    public void configureMockito() throws BadRequestException, ResourceNotFoundException {
        Mockito.when(odontologoService.crear(odontologo)).thenReturn(odontologoExistente);
        Mockito.when(odontologoService.buscarPorId(999)).thenThrow(new ResourceNotFoundException("El odontólogo no existe"));
        Mockito.when(odontologoService.buscarPorId(1)).thenReturn(odontologoExistente);
        Mockito.when(odontologoService.buscar("Pepe")).thenReturn(List.of(odontologoExistente));
        Mockito.when(odontologoService.buscar(123456)).thenReturn(odontologoExistente);
        Mockito.when(odontologoService.actualizar(odontologoInexistente)).thenThrow(new ResourceNotFoundException("El odontólogo no existe"));
        Mockito.when(odontologoService.actualizar(odontologoExistente)).thenReturn(odontologoExistente);
        doThrow(new ResourceNotFoundException("No existe ningún odontólogo con id: 999")).when(odontologoService).eliminar(999);
    }

    @Test
    public void test01registrarOdontologo() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(Mapper.mapObjectToJson(odontologo)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        assertEquals(Mapper.mapObjectToJson(odontologoExistente), response.getResponse().getContentAsString());
    }

    @Test
    public void test02buscarOdontologoPorIdInexistenteDevuelveNotFound() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/{id}", "999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        assertEquals("El odontólogo no existe", response.getResponse().getContentAsString());
    }

    @Test
    public void test03buscarOdontologoPorIdExistenteDevuelveOk() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/{id}", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(response.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void test04buscarOdontologosPorNombreInexistenteDevuelveOkYArrayVacio() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/odontologos")
                        .param("nombre", "José")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    public void test05buscarOdontologosPorNombreExistenteDevuelveOkYArrayNoVacio() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/odontologos")
                        .param("nombre", "Pepe")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertNotEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    public void test06buscarOdontologosPorNombreYApellidoInexistenteDevuelveOkYArrayVacio() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/odontologos")
                        .param("nombre", "José")
                        .param("apellido", "Pérez")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    public void test07buscarOdontologoPorMatriculaExistenteDevuelveOk() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/odontologos")
                        .param("matricula", "123456")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(response.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void test08ActualizarOdontologoInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(Mapper.mapObjectToJson(odontologoInexistente))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void test09ActualizarOdontologoExistenteDevuelveOk() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .put("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(Mapper.mapObjectToJson(odontologoExistente))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(Mapper.mapObjectToJson(odontologoExistente), response.getResponse().getContentAsString());
    }

    @Test
    public void test10eliminarOdontologoPorIdInexistenteDevuelveNotFound() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/odontologos/{id}", "999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        assertEquals("No existe ningún odontólogo con id: 999", response.getResponse().getContentAsString());
    }

    @Test
    public void test11eliminarOdontologoPorIdExistenteDevuelveOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/odontologos/{id}", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void test12buscarTodosLosOdontologos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/odontologos"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}