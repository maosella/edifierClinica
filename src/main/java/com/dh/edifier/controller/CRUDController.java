package com.dh.edifier.controller;

import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CRUDController<T> {
    ResponseEntity<?> registrar(@RequestBody T t) throws BadRequestException, ResourceNotFoundException;

    ResponseEntity<?> buscarPorId(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException;

    ResponseEntity<?> actualizar(@RequestBody T t) throws BadRequestException, ResourceNotFoundException;

    ResponseEntity<String> eliminar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException;

    ResponseEntity<?> buscarTodos();


}
