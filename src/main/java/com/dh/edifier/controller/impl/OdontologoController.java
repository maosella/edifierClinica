package com.dh.edifier.controller.impl;

import com.dh.edifier.controller.CRUDController;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.OdontologoDTO;
import com.dh.edifier.service.IOdontologoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController implements CRUDController<OdontologoDTO> {

    @Qualifier("odontologoService")
    private final IOdontologoService odontologoService;

    @Autowired
    public OdontologoController(IOdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }


    @Override
    @ApiOperation(value = "Crea un nuevo odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping()
    public ResponseEntity<OdontologoDTO> registrar(@RequestBody OdontologoDTO odontologo) throws BadRequestException, ResourceNotFoundException {
        OdontologoDTO odontologoInsertado = odontologoService.crear(odontologo);
        return ResponseEntity.ok(odontologoInsertado);
    }

    @Override
    @ApiOperation(value = "Busca un odontólogo por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OdontologoDTO> buscarPorId(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        OdontologoDTO odontologo = odontologoService.buscarPorId(id);
        return ResponseEntity.ok(odontologo);
    }

    @ApiOperation(value = "Busca un odontólogo por matrícula")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping(params = "matricula")
    public ResponseEntity<OdontologoDTO> buscar(@RequestParam Integer matricula) throws ResourceNotFoundException, BadRequestException{
        OdontologoDTO odontologo = odontologoService.buscar(matricula);
        return ResponseEntity.ok(odontologo);
    }

    @ApiOperation(value = "Busca odontólogos por nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping(params = "nombre")
    public ResponseEntity<List<OdontologoDTO>> buscar(@RequestParam String nombre){
        List<OdontologoDTO> odontologos = odontologoService.buscar(nombre);
        return ResponseEntity.ok(odontologos);
    }

    @ApiOperation(value = "Busca odontólogos por nombre y apellido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping(params = {"nombre", "apellido"})
    public ResponseEntity<List<OdontologoDTO>> buscar(@RequestParam String nombre, @RequestParam String apellido){
        List<OdontologoDTO> odontologos = odontologoService.buscar(nombre, apellido);
        return ResponseEntity.ok(odontologos);
    }

    @Override
    @ApiOperation(value = "Actualiza un odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PutMapping()
    public ResponseEntity<OdontologoDTO> actualizar(@RequestBody OdontologoDTO odontologo) throws BadRequestException, ResourceNotFoundException {
        OdontologoDTO actualizado = odontologoService.actualizar(odontologo);
        return ResponseEntity.ok(actualizado);
    }

    @Override
    @ApiOperation(value = "Elimina un odontólogo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        odontologoService.eliminar(id);
        return ResponseEntity.ok("Se eliminó el odontólogo con id " + id);
    }

    @Override
    @ApiOperation(value = "Busca todos los odontólogos")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping
    public ResponseEntity<?> buscarTodos() {
        return ResponseEntity.ok(odontologoService.consultarTodos());
    }
}
