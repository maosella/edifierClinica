package com.dh.edifier.controllers;

import com.dh.edifier.controller.impl.TurnoController;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.GlobalExceptionsHandler;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.DomicilioDTO;
import com.dh.edifier.model.OdontologoDTO;
import com.dh.edifier.model.PacienteDTO;
import com.dh.edifier.model.TurnoDTO;
import com.dh.edifier.service.ITurnoService;
import org.junit.Assert;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(TurnoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TurnoControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ITurnoService turnoService;

    @InjectMocks
    private TurnoController turnoController;


    private OdontologoDTO odontologo;
    private OdontologoDTO odontologoInexistente;
    private OdontologoDTO odontologoExistente;
    private PacienteDTO paciente;
    private PacienteDTO pacienteInexistente;
    private PacienteDTO pacienteExistente;
    private TurnoDTO turno;
    private TurnoDTO turnoVacio;
    private TurnoDTO turnoExistente;
    private TurnoDTO turnoInexistente;

    @Before
    public void reset() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(turnoController).setControllerAdvice(GlobalExceptionsHandler.class).build();
        resetearOdontologos();
        resetearTurnos();
        turno = new TurnoDTO(LocalDateTime.now(), paciente, odontologo);
        turnoVacio = new TurnoDTO(LocalDateTime.now(), null, null);
        turnoExistente = new TurnoDTO(LocalDateTime.now(), pacienteExistente, odontologoExistente);
        turnoInexistente = new TurnoDTO(LocalDateTime.now(), pacienteInexistente, odontologoInexistente);
        configureMockito();
    }

    private void resetearOdontologos() {
        odontologo = new OdontologoDTO();
        odontologoInexistente = new OdontologoDTO(123456788, "Pepe", "Pepardo", 123456);
        odontologoExistente = new OdontologoDTO(123456788, "Pepe", "Pepardo", 123456);
        odontologo.setId(1);
        odontologoExistente.setId(1);
        odontologoExistente.setId(1);
    }

    private void resetearTurnos() {
        DomicilioDTO domicilioConId = new DomicilioDTO("Calle Falsa", 123, "Springfield", "Springfield");
        domicilioConId.setId(1);
        paciente = new PacienteDTO();
        pacienteInexistente = new PacienteDTO("Pepe", "Pepardo", 123456789, domicilioConId);
        pacienteExistente = new PacienteDTO("Pepe", "Pepardo", 123456789, domicilioConId);
        paciente.setId(1);
        pacienteExistente.setId(1);
        pacienteExistente.setFechaIngreso(LocalDate.now());
        pacienteInexistente.setFechaIngreso(LocalDate.now());
    }

    private void configureMockito() throws Exception {
        Mockito.when(turnoService.crear(turno)).thenReturn(turnoExistente);
        Mockito.when(turnoService.crear(turnoInexistente)).thenThrow(new BadRequestException("El turno u odontólogo no existen"));
        Mockito.when(turnoService.crear(turnoVacio)).thenThrow(new BadRequestException("El turno u odontólogo es null"));
        Mockito.when(turnoService.buscarPorId(999)).thenThrow(new ResourceNotFoundException("No se encontró el turno con id 999"));
        Mockito.when(turnoService.buscarPorId(1)).thenReturn(turnoExistente);
        Mockito.when(turnoService.buscar("Pepe", "Pepardo")).thenReturn(List.of(turnoExistente));
        Mockito.when(turnoService.buscar(123456, 123456789)).thenReturn(List.of(turnoExistente));
        Mockito.when(turnoService.actualizar(turnoInexistente)).thenThrow(new ResourceNotFoundException("No se encontró el turno con id 999"));
        Mockito.when(turnoService.actualizar(turnoExistente)).thenReturn(turnoExistente);
        doThrow(new ResourceNotFoundException("No existe ningún turno con id: 999")).when(turnoService).eliminar(999);
    }

    @Test
    public void test01registrarTurno() throws Exception {
        MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(Mapper.mapObjectToJson(turno)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assert.assertFalse(response.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void test02noSePuedeRegistrarTurnoSinPacienteUOdontologo() throws Exception {
        MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(Mapper.mapObjectToJson(turnoVacio)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        assertFalse(response.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void test03noSePuedeRegistrarTurnoSinLosIdsDePacienteYOdontologo() throws Exception {
        MvcResult response = this.mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(Mapper.mapObjectToJson(turnoInexistente)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        assertFalse(response.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void test04buscarTurnoPorIdInexistenteDevuelveNotFound() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/turnos/{id}", "999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        assertEquals("No se encontró el turno con id 999", response.getResponse().getContentAsString());
    }


    @Test
    public void test05buscarPacientePorIdExistenteDevuelveOk() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/turnos/{id}", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(Mapper.mapObjectToJson(turnoExistente), response.getResponse().getContentAsString());
    }

    @Test
    public void test06buscarTurnosPorNombreInexistenteDevuelveOkYArrayVacio() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/turnos")
                        .param("nombre", "José")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    public void test07buscarTurnosPorNombreYApellidoDeOdontologoExistenteDevuelveOkYArrayNoVacio() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/turnos")
                        .param("nombreOdontologo", "Pepe")
                        .param("apellidoOdontologo", "Pepardo")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertNotEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    public void test08buscarTurnosPorNombreYApellidoInexistenteDevuelveOkYArrayVacio() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/turnos")
                        .param("nombre", "José")
                        .param("apellido", "Pérez")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    public void test09buscarPacientePorDniYMatriculaExistentesDevuelveOkYArrayNoVacio() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/turnos")
                        .param("dni", "123456789")
                        .param("matricula", "123456")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertNotEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    public void test10ActualizarPacienteInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(Mapper.mapObjectToJson(turnoInexistente))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void test11ActualizarPacienteExistenteDevuelveOk() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders
                        .put("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Mapper.mapObjectToJson(turnoExistente))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(Mapper.mapObjectToJson(turnoExistente), response.getResponse().getContentAsString());
    }

    @Test
    public void test12eliminarPacientePorIdInexistenteDevuelveNotFound() throws Exception {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/turnos/{id}", "999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        assertEquals("No existe ningún turno con id: 999", response.getResponse().getContentAsString());
    }

    @Test
    public void test12eliminarPacientePorIdExistenteDevuelveOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/turnos/{id}", "1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    public void test14buscarTodosLosTurnos() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/turnos"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }
}
