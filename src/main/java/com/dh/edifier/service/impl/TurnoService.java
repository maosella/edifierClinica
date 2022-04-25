package com.dh.edifier.service.impl;

import com.dh.edifier.config.SpringConfig;
import com.dh.edifier.entities.Turno;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.OdontologoDTO;
import com.dh.edifier.model.PacienteDTO;
import com.dh.edifier.model.TurnoDTO;
import com.dh.edifier.repository.ITurnoRepository;
import com.dh.edifier.service.CRUDService;
import com.dh.edifier.service.ITurnoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurnoService implements ITurnoService {

    @Autowired
    private CRUDService<OdontologoDTO> odontologoService;
    @Autowired
    private CRUDService<PacienteDTO> pacienteService;
    private final ITurnoRepository turnoRepository;
    @Autowired
    private SpringConfig springConfig;

    @Autowired
    public TurnoService(ITurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    public List<TurnoDTO> buscar(String nombrePaciente, String apellidoPaciente, String nombreOdontologo, String apellidoOdontologo) {
        List<Turno> turnos = turnoRepository.buscar(nombrePaciente, apellidoPaciente, nombreOdontologo, apellidoOdontologo)
                .orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDTO.class);
    }

    @Override
    public List<TurnoDTO> buscar(String nombreOdontologo, String apellidoOdontologo) {
        List<Turno> turnos = turnoRepository.buscar(nombreOdontologo, apellidoOdontologo).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDTO.class);
    }

    @Override
    public List<TurnoDTO> buscar(Integer matricula, Integer dni) {
        List<Turno> turnos = turnoRepository.buscar(matricula, dni).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDTO.class);
    }

    @Override
    public TurnoDTO buscarPorId(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del turno no puede ser null ni negativo");
        Turno turno = turnoRepository.findById(id).orElse(null);
        if (turno == null)
            throw new ResourceNotFoundException("No se encontró el turno con id " + id);
        return Mapper.map(springConfig.getModelMapper(), turno, TurnoDTO.class);
    }

    @Override
    public TurnoDTO crear(TurnoDTO turnoDTO) throws BadRequestException, ResourceNotFoundException {
        if (turnoDTO.getPaciente() == null || turnoDTO.getOdontologo() == null)
            throw new BadRequestException("El paciente u odontólogo es null");
        Integer pacienteId = turnoDTO.getPaciente().getId();
        Integer odontologoId = turnoDTO.getOdontologo().getId();
        if (this.existenPacienteYOdontologo(pacienteId, odontologoId)){
            if (this.sePuedeSacarTurno(turnoDTO)){
                Turno turno = Mapper.map(springConfig.getModelMapper(), turnoDTO, Turno.class);
                turnoDTO = Mapper.map(springConfig.getModelMapper(), turnoRepository.save(turno), TurnoDTO.class);
                turnoDTO.setPaciente(pacienteService.buscarPorId(pacienteId));
                turnoDTO.setOdontologo(odontologoService.buscarPorId(odontologoId));
            } else {
                throw new BadRequestException("El odontólogo ya tiene un turno programado para ese día en ese horario");
            }
        }
        return turnoDTO;
    }

    @SneakyThrows
    @Override
    public TurnoDTO actualizar(TurnoDTO turnoDTO) {
        TurnoDTO turnoActualizado;
        if (turnoDTO == null)
            throw new BadRequestException("No se pudo actualizar el turno null");
        if (turnoDTO.getId() == null)
            throw new BadRequestException("El id del turno a actualizar no puede ser null");
        if (turnoDTO.getPaciente() == null || turnoDTO.getOdontologo() == null)
            throw new BadRequestException("El paciente u odontólogo es null");
        Optional<Turno> turnoEnBD = turnoRepository.findById(turnoDTO.getId());

        if (turnoEnBD.isPresent()){
            if (this.sePuedeSacarTurno(turnoDTO)){
                Turno actualizado = this.actualizar(turnoEnBD.get(), turnoDTO);
                Turno guardado = turnoRepository.save(actualizado);
                turnoActualizado = Mapper.map(springConfig.getModelMapper(), guardado, TurnoDTO.class);
            } else {
                throw new BadRequestException("El odontólogo ya tiene un turno programado para ese día en ese horario");
            }
        } else {
            throw new ResourceNotFoundException("El odontólogo no existe");
        }
        return turnoActualizado;
    }

    @Override
    public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del turno no puede ser null ni negativo");
        if (!turnoRepository.existsById(id))
            throw new ResourceNotFoundException("No existe ningún turno con id: " + id);
        turnoRepository.deleteById(id);
    }

    @Override
    public List<TurnoDTO> consultarTodos() {
        List<Turno> turnos = turnoRepository.findAll();
        return Mapper.mapList(springConfig.getModelMapper(), turnos, TurnoDTO.class);
    }

    @Override
    public List<TurnoDTO> consultarProximosTurnos(LocalDateTime desde, Integer cantidadDias) {
        LocalDateTime hasta = desde.plusDays(cantidadDias);
        List<Turno> filtrados = turnoRepository.findAll()
                .stream()
                .filter(turno -> (
                        (turno.getFecha().isAfter(desde)
                                || turno.getFecha().equals(desde)
                                && turno.getFecha().isBefore(hasta)))
                )
                .collect(Collectors.toList());
        return Mapper.mapList(springConfig.getModelMapper(), filtrados, TurnoDTO.class);
    }

    private Turno actualizar(Turno turno, TurnoDTO turnoDTO) throws Exception {
        if (turnoDTO.getFecha() != null) {
            turno.setFecha(turnoDTO.getFecha());
        }
        if (turnoDTO.getPaciente() != null) {
            pacienteService.actualizar(turnoDTO.getPaciente());
        }
        if (turnoDTO.getOdontologo() != null) {
            odontologoService.actualizar(turnoDTO.getOdontologo());
        }
        return turno;
    }

    private boolean existenPacienteYOdontologo(Integer pacienteId, Integer odontologoId) throws  BadRequestException, ResourceNotFoundException {
        PacienteDTO p = pacienteService.buscarPorId(pacienteId);
        OdontologoDTO o = odontologoService.buscarPorId(odontologoId);
        return (p != null && o != null);
    }

    private boolean sePuedeSacarTurno(TurnoDTO turnoDTO) throws BadRequestException, ResourceNotFoundException {
        OdontologoDTO odontologoDTO = odontologoService.buscarPorId(turnoDTO.getOdontologo().getId());
        return turnoRepository.findAll()
                .stream()
                .map(turno -> Mapper.map(springConfig.getModelMapper(), turno, TurnoDTO.class))
                .noneMatch(t -> t.getOdontologo().equals(odontologoDTO) && t.getFecha().equals(turnoDTO.getFecha()));
    }
}
