package com.dh.edifier.service.impl;

import com.dh.edifier.config.SpringConfig;
import com.dh.edifier.entities.Domicilio;
import com.dh.edifier.entities.Paciente;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.DomicilioDTO;
import com.dh.edifier.model.PacienteDTO;
import com.dh.edifier.repository.IPacienteRepository;
import com.dh.edifier.service.IDomicilioService;
import com.dh.edifier.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {

    private final IPacienteRepository pacienteRepository;
    private final IDomicilioService domicilioService;
    private final SpringConfig springConfig;

    @Autowired
    public PacienteService(IPacienteRepository pacienteRepository, IDomicilioService domicilioService, SpringConfig springConfig) {
        this.pacienteRepository = pacienteRepository;
        this.domicilioService = domicilioService;
        this.springConfig = springConfig;
    }

    @Override
    public PacienteDTO buscar(Integer dni) throws ResourceNotFoundException, BadRequestException {
        if (dni == null)
            throw new BadRequestException("El DNI del paciente no puede ser null");
        Paciente paciente = pacienteRepository.buscar(dni).orElse(null);
        if (paciente == null)
            throw new ResourceNotFoundException("No se encontró el paciente del DNI " + dni);
        return springConfig.getModelMapper().map(paciente, PacienteDTO.class);
    }

    @Override
    public List<PacienteDTO> buscar(String nombre) {
        List<Paciente> pacientes = pacienteRepository.buscar(nombre).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), pacientes, PacienteDTO.class);
    }

    @Override
    public List<PacienteDTO> buscar(String nombre, String apellido) {
        List<Paciente> pacientes = pacienteRepository.buscar(nombre, apellido).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), pacientes, PacienteDTO.class);
    }

    @Override
    public PacienteDTO buscarPorId(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del paciente no puede ser null ni negativo");
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente == null)
            throw new ResourceNotFoundException("No se encontró el paciente con id " + id);
        return springConfig.getModelMapper().map(paciente, PacienteDTO.class);
    }

    @Override
    public PacienteDTO crear(PacienteDTO pacienteDTO) throws BadRequestException, ResourceNotFoundException {
        if (pacienteDTO == null)
            throw new BadRequestException("No se puede crear un paciente null");
        Paciente paciente = springConfig.getModelMapper().map(pacienteDTO, Paciente.class);
        return springConfig.getModelMapper().map(pacienteRepository.save(paciente), PacienteDTO.class);
    }

    @Override
    public PacienteDTO actualizar(PacienteDTO pacienteDTO) throws BadRequestException, ResourceNotFoundException {
        if (pacienteDTO == null)
            throw new BadRequestException("No se pudo actualizar en paciente null");
        if (pacienteDTO.getId() == null)
            throw new BadRequestException("El id del paciente a actualizar no puede ser null");
        Optional<Paciente> pacienteEnBD = pacienteRepository.findById(pacienteDTO.getId());
        if (pacienteEnBD.isPresent()){
            Paciente actualizado = this.actualizar(pacienteEnBD.get(), pacienteDTO);
            pacienteDTO = springConfig.getModelMapper().map(pacienteRepository.save(actualizado), PacienteDTO.class);
        } else {
            throw new ResourceNotFoundException("El paciente no existe");
        }
        return pacienteDTO;
    }

    @Override
    public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del paciente no puede ser null ni negativo");
        if (!pacienteRepository.existsById(id))
            throw new ResourceNotFoundException("No existe ningún paciente con id " + id);
        pacienteRepository.deleteById(id);
    }


    @Override
    public List<PacienteDTO> consultarTodos() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        return Mapper.mapList(springConfig.getModelMapper(), pacientes, PacienteDTO.class);
    }

    private Paciente actualizar(Paciente paciente, PacienteDTO pacienteDTO) throws BadRequestException, ResourceNotFoundException{
        if (pacienteDTO.getNombre() != null){
            paciente.setNombre(pacienteDTO.getNombre());
        }
        if (pacienteDTO.getApellido() != null){
            paciente.setApellido(pacienteDTO.getApellido());
        }
        if (pacienteDTO.getDni() != null){
            paciente.setDni(pacienteDTO.getDni());
        }
        if (pacienteDTO.getDomicilio() != null){
            DomicilioDTO actualizado = domicilioService.actualizar(pacienteDTO.getDomicilio());
            paciente.setDomicilio(springConfig.getModelMapper().map(actualizado, Domicilio.class));
        }
        return paciente;
    }
}
