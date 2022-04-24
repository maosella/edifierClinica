package com.dh.edifier.service;

import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.PacienteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPacienteService extends CRUDService<PacienteDTO> {

    PacienteDTO buscar(Integer dni) throws ResourceNotFoundException, BadRequestException;

    List<PacienteDTO> buscar(String nombre);

    List<PacienteDTO> buscar(String nombre, String apellido);

}
