package com.dh.edifier.service;

import com.dh.edifier.model.TurnoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface ITurnoService extends CRUDService<TurnoDTO> {
    List<TurnoDTO> buscar(String nombrePaciente, String apellidoPaciente, String nombreOdontologo, String apellidoOdontologo);

    List<TurnoDTO> buscar(String nombreOdontologo, String apellidoOdontologo);

    List<TurnoDTO> buscar(Integer matricula, Integer dni);

    List<TurnoDTO> consultarProximosTurnos(LocalDateTime desde, Integer cantidadDias);
}
