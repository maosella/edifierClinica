package com.dh.edifier.service;

import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.DomicilioDTO;
import com.dh.edifier.model.OdontologoDTO;
import com.dh.edifier.model.PacienteDTO;
import com.dh.edifier.model.TurnoDTO;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
public class TurnoServiceTests {

    @Autowired
    private IPacienteService pacienteService;
    private PacienteDTO paciente;

    @Autowired
    private IOdontologoService odontologoService;
    private OdontologoDTO odontologo;

    @Autowired
    private ITurnoService turnoService;

    @BeforeEach
    public void setUp() {
        DomicilioDTO domicilio = new DomicilioDTO();
        domicilio.setCalle("Calle Falsa");
        domicilio.setNumero(123);
        domicilio.setLocalidad("Springfield");
        domicilio.setProvincia("Springfield");

        paciente = new PacienteDTO();
        paciente.setNombre("Pepe");
        paciente.setApellido("Pepardo");
        paciente.setDni(123456789);
        paciente.setDomicilio(domicilio);

        odontologo = new OdontologoDTO();
        odontologo.setNombre("Pepo");
        odontologo.setApellido("Pepardo");
        odontologo.setMatricula(123456);
    }

    @Test
    public void test01CrearTurnoConPacienteYOdontologoExistente() throws BadRequestException, ResourceNotFoundException {
        PacienteDTO pacienteCreado = pacienteService.crear(paciente);
        OdontologoDTO odontologoCreado = odontologoService.crear(odontologo);
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.now());
        turno.setPaciente(pacienteCreado);
        turno.setOdontologo(odontologoCreado);
        TurnoDTO turnoCreado = turnoService.crear(turno);
        assertNotNull(turnoService.buscarPorId(turnoCreado.getId()));
    }

    @Test
    public void test02CrearTurnoConPacienteYOdontologoInexistente() throws BadRequestException, ResourceNotFoundException {
        PacienteDTO pacienteCreado = pacienteService.crear(paciente);
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.now());
        turno.setPaciente(pacienteCreado);
        turno.setOdontologo(odontologo);
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test04CrearTurnoConPacienteInexistenteOdontologoExistente() throws BadRequestException, ResourceNotFoundException {
        OdontologoDTO odontologoCreado = odontologoService.crear(odontologo);
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.now());
        turno.setPaciente(paciente);
        turno.setOdontologo(odontologoCreado);
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test05CrearTurnoSinPacienteNiOdontologo() {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.now());
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test06NoSePuedeCrearTurnoConHorarioRepetidoParaOdontologo() throws BadRequestException, ResourceNotFoundException {
        PacienteDTO pacienteCreado = pacienteService.crear(paciente);
        OdontologoDTO odontologoCreado = odontologoService.crear(odontologo);
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteCreado);
        turno.setOdontologo(odontologoCreado);
        TurnoDTO turnoCreado = turnoService.crear(turno);
        assertNotNull(turnoService.buscarPorId(turnoCreado.getId()));
        assertThrows(BadRequestException.class, () -> turnoService.crear(turno));
    }

    @Test
    public void test07ActualizarTurno() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        TurnoDTO turnoCreado = turnoService.crear(turno);
        turno.setId(turnoCreado.getId());
        turno.setFecha(LocalDateTime.of(2021, 9, 27, 18, 30));
        TurnoDTO turnoActualizado = turnoService.actualizar(turno);
        assertNotEquals(turnoActualizado, turnoCreado);
    }

    @Test
    public void test08NoSePuedeActualizarTurnoSinId() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turno.setFecha(LocalDateTime.of(2021, 9, 27, 18, 30));
        assertThrows(BadRequestException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test08NoSePuedeActualizarTurnoConIdInexistente() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService. crear(odontologo));
        turnoService.crear(turno);
        turno.setId(51);
        turno.setFecha(LocalDateTime.of(2021, 9, 27, 18, 30));
        assertThrows(ResourceNotFoundException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test10NoSePuedeActualizarTurnoSinPaciente() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turno.setPaciente(null);
        turno.setFecha(LocalDateTime.of(2021, 9, 27, 18, 30));
        assertThrows(BadRequestException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test11NoSePuedeActualizarTurnoSinOdontologo() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turno.setOdontologo(null);
        turno.setFecha(LocalDateTime.of(2021, 9, 27, 18, 30));
        assertThrows(BadRequestException.class, () -> turnoService.actualizar(turno));
    }

    @Test
    public void test12EliminarTurno() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        turnoService.eliminar(1);
        assertThrows(ResourceNotFoundException.class, () -> turnoService.buscarPorId(1));
    }

    @Test
    public void test13ConsultarTodos() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        assertNotEquals(0, turnoService.consultarTodos().size());
    }

    @Test
    public void test13ConsultarTodosPorNombreYApellidoOdontologo() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        assertNotEquals(0, turnoService.buscar(odontologo.getNombre(), odontologo.getApellido()).size());
    }

    @Test
    public void test14ConsultarTodosPorDniPacienteYMatriculaOdontologo() throws BadRequestException, ResourceNotFoundException {
        TurnoDTO turno = new TurnoDTO();
        turno.setFecha(LocalDateTime.of(2021, 9, 20, 18, 30));
        turno.setPaciente(pacienteService.crear(paciente));
        turno.setOdontologo(odontologoService.crear(odontologo));
        turnoService.crear(turno);
        assertNotEquals(0, turnoService.buscar(odontologo.getMatricula(), paciente.getDni()).size());
    }
}
