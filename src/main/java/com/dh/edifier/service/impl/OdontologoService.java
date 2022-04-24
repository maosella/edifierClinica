package com.dh.edifier.service.impl;

import com.dh.edifier.config.SpringConfig;
import com.dh.edifier.entities.Odontologo;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.OdontologoDTO;
import com.dh.edifier.repository.IOdontologoRepository;
import com.dh.edifier.service.IOdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService implements IOdontologoService {

    private final IOdontologoRepository odontologoRepository;
    private final SpringConfig springConfig;

    @Autowired
    public OdontologoService(IOdontologoRepository odontologoRepository, SpringConfig springConfig) {
        this.odontologoRepository = odontologoRepository;
        this.springConfig = springConfig;
    }

    @Override
    public OdontologoDTO buscar(Integer matricula) throws ResourceNotFoundException, BadRequestException {
        if (matricula == null)
            throw new BadRequestException("La matrícula del odontólogo no puede ser null");
        Odontologo odontologo = odontologoRepository.buscar(matricula).orElse(null);
        if (odontologo == null)
            throw new ResourceNotFoundException("El odontólogo con matrícula " + matricula + "no existe");

        return springConfig.getModelMapper().map(odontologo, OdontologoDTO.class);
    }

    @Override
    public List<OdontologoDTO> buscar(String nombre) {
        List<Odontologo> odontologos = odontologoRepository.buscar(nombre).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), odontologos, OdontologoDTO.class);
    }
    @Override
    public List<OdontologoDTO> buscar(String nombre, String apellido) {
        List<Odontologo> odontologos = odontologoRepository.buscar(nombre, apellido).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), odontologos, OdontologoDTO.class);
    }

    @Override
    public OdontologoDTO buscarPorId(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del odontólogo no puede ser null ni negativo");
        Odontologo odontologo = odontologoRepository.findById(id).orElse(null);
        if (odontologo == null)
            throw new ResourceNotFoundException("El odontólogo no existe");
        return springConfig.getModelMapper().map(odontologo, OdontologoDTO.class);
    }

    @Override
    public OdontologoDTO crear(OdontologoDTO odontologoDTO) throws BadRequestException{
       if (odontologoDTO == null)
           throw new BadRequestException("El odontólogo no puede ser null");
       Odontologo odontologo = springConfig.getModelMapper().map(odontologoDTO, Odontologo.class);
       return springConfig.getModelMapper().map(odontologoRepository.save(odontologo), OdontologoDTO.class);
    }

    @Override
    public OdontologoDTO actualizar(OdontologoDTO odontologoDTO) throws BadRequestException, ResourceNotFoundException {
        OdontologoDTO odontologoActualizado;
        if (odontologoDTO == null)
            throw new BadRequestException("El odontólogo no puede ser null");
        if (odontologoDTO.getId() == null)
            throw new BadRequestException("El id del odontólogo no puede ser null");
        Optional<Odontologo> odontologoEnBD = odontologoRepository.findById(odontologoDTO.getId());
        if (odontologoEnBD.isPresent()) {
            Odontologo actualizado = this.actualizar(odontologoEnBD.get(), odontologoDTO);
            Odontologo guardado = odontologoRepository.save(actualizado);
            odontologoActualizado = springConfig.getModelMapper().map(guardado, OdontologoDTO.class);
        } else {
            throw new ResourceNotFoundException("El odontólogo no existe");
        }
        return odontologoActualizado;
    }

    @Override
    public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del odontólogo no puede ser null ni negativo");
        if (!odontologoRepository.existsById(id))
            throw new ResourceNotFoundException("No existe ningún odontólogo con id: " + id);
        odontologoRepository.deleteById(id);
    }

    @Override
    public List<OdontologoDTO> consultarTodos() {
        List<Odontologo> odontologos = odontologoRepository.findAll();
        return Mapper.mapList(springConfig.getModelMapper(), odontologos, OdontologoDTO.class);
    }

    private Odontologo actualizar(Odontologo odontologo, OdontologoDTO odontologoDTO){
        if (odontologoDTO.getNombre() != null){
            odontologo.setNombre(odontologoDTO.getNombre());
        }
        if (odontologoDTO.getApellido() != null){
            odontologo.setApellido(odontologoDTO.getApellido());
        }
        if (odontologoDTO.getMatricula() != null) {
            odontologo.setMatricula(odontologoDTO.getMatricula());
        }
        return odontologo;
    }
}