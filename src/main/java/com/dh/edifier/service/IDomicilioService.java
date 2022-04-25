package com.dh.edifier.service;

import com.dh.edifier.model.DomicilioDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDomicilioService extends CRUDService<DomicilioDTO>{

    List<DomicilioDTO> buscar(String calle);

    List<DomicilioDTO> buscar(String calle, Integer numero);

    DomicilioDTO buscar(String calle, Integer numero, String localidad, String provincia) throws Exception;


}
