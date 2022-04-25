package com.dh.edifier.service.impl;

import com.dh.edifier.config.SpringConfig;
import com.dh.edifier.entities.Domicilio;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.DomicilioDTO;
import com.dh.edifier.repository.IDomicilioRepository;
import com.dh.edifier.service.IDomicilioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DomicilioService implements IDomicilioService {

    private final IDomicilioRepository domicilioRepository;
    private final SpringConfig springConfig;

    @Autowired
    public DomicilioService(IDomicilioRepository domicilioRepository, SpringConfig springConfig) {
        this.domicilioRepository = domicilioRepository;
        this.springConfig = springConfig;
    }

    public List<DomicilioDTO> buscar(String calle){
        List<Domicilio> domicilios = domicilioRepository.buscar(calle).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), domicilios, DomicilioDTO.class);
    }

    public List<DomicilioDTO> buscar(String calle, Integer numero){
        List<Domicilio> domicilios = domicilioRepository.buscar(calle, numero).orElse(new ArrayList<>());
        return Mapper.mapList(springConfig.getModelMapper(), domicilios, DomicilioDTO.class);
    }
    public DomicilioDTO buscar(String calle, Integer numero, String localidad, String provincia) throws ResourceNotFoundException {
        Domicilio domicilio = domicilioRepository.buscar(calle, numero, localidad, provincia).orElse(null);
        if (domicilio == null)
            throw new ResourceNotFoundException("No se encontró el domicilio");

        return springConfig.getModelMapper().map(domicilio, DomicilioDTO.class);
    }


    @Override
    public DomicilioDTO buscarPorId(Integer id) throws ResourceNotFoundException, BadRequestException {
        if (id == null || id < 1)
            throw new BadRequestException("El id del domicilio no puede ser null ni negativo");
        Domicilio domicilio = domicilioRepository.findById(id).orElse(null);
        if (domicilio == null)
            throw new ResourceNotFoundException("No se encontró el domicilio con id " + id);

        return springConfig.getModelMapper().map(domicilio, DomicilioDTO.class);
    }

    @Override
    public DomicilioDTO crear(DomicilioDTO domicilioDTO) throws BadRequestException {
        if (domicilioDTO == null)
            throw new BadRequestException("El domicilio no puede ser null");

        Domicilio domicilio = springConfig.getModelMapper().map(domicilioDTO, Domicilio.class);
        domicilioDTO = springConfig.getModelMapper().map(domicilioRepository.save(domicilio), DomicilioDTO.class);
        return domicilioDTO;
    }

    @Override
    public DomicilioDTO actualizar(DomicilioDTO domicilioDTO) throws BadRequestException {
        DomicilioDTO domicilioActualizado = null;
        if (domicilioDTO == null)
            throw new BadRequestException("No se pudo actualizar el domicilio null");
        if (domicilioDTO.getId() == null)
            throw new BadRequestException("El id del domicilio a actualizar no puede ser null");

        Optional<Domicilio> domicilioEnBD = domicilioRepository.findById(domicilioDTO.getId());
        if (domicilioEnBD.isPresent()) {
            Domicilio actualizado = this.actualizar(domicilioEnBD.get(), domicilioDTO);
            Domicilio guardado = domicilioRepository.save(actualizado);
            domicilioActualizado = springConfig.getModelMapper().map(guardado, DomicilioDTO.class);
        }
        return domicilioActualizado;
    }

    @Override
    public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException{
        if (id == null || id < 1)
            throw new BadRequestException("El id del domicilio no puede ser null ni negativo");
        if (!domicilioRepository.existsById(id))
            throw new ResourceNotFoundException("No existe ningún domicilio con id: " + id);
        domicilioRepository.deleteById(id);
    }

    @Override
    public List<DomicilioDTO> consultarTodos() {
        List<Domicilio> domicilios = domicilioRepository.findAll();
        return Mapper.mapList(springConfig.getModelMapper(), domicilios, DomicilioDTO.class);
    }

    private Domicilio actualizar(Domicilio domicilio, DomicilioDTO domicilioDTO){
        if  (domicilioDTO.getCalle() != null) {
            domicilio.setCalle(domicilioDTO.getCalle());
        }
        if (domicilioDTO.getNumero() != null) {
            domicilio.setNumero(domicilioDTO.getNumero());
        }
        if (domicilioDTO.getLocalidad() != null) {
            domicilio.setLocalidad(domicilioDTO.getLocalidad());
        }
        if (domicilioDTO.getProvincia() != null) {
            domicilio.setProvincia(domicilioDTO.getProvincia());
        }
        return domicilio;
    }
}
