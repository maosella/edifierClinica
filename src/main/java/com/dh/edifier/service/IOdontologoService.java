package com.dh.edifier.service;

import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.OdontologoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IOdontologoService extends CRUDService<OdontologoDTO> {

    OdontologoDTO buscar(Integer matricula) throws ResourceNotFoundException, BadRequestException;

    List<OdontologoDTO> buscar(String nombre);

    List<OdontologoDTO> buscar(String nombre, String apellido);


}
