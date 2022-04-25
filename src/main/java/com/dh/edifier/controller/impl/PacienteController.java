package com.dh.edifier.controller.impl;

import com.dh.edifier.controller.CRUDController;
import com.dh.edifier.exceptions.BadRequestException;
import com.dh.edifier.exceptions.ResourceNotFoundException;
import com.dh.edifier.model.PacienteDTO;
import com.dh.edifier.service.IPacienteService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController implements CRUDController<PacienteDTO> {

    @Qualifier("pacienteService")
    private final IPacienteService pacienteService;

    @Autowired
    public PacienteController(IPacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @Override
    @ApiOperation(value = "Crea un nuevo paciente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping()
    public ResponseEntity<PacienteDTO> registrar(@RequestBody PacienteDTO paciente) throws BadRequestException, ResourceNotFoundException {
        paciente.setFechaIngreso(LocalDate.now());
        PacienteDTO pacienteInsertado = pacienteService.crear(paciente);
        return ResponseEntity.ok(pacienteInsertado);
    }

    @Override
    @ApiOperation(value = "Busca un paciente por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        PacienteDTO paciente = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(paciente);
    }

    @GetMapping(params = "dni")
    @ApiOperation(value = "Busca un paciente por DNI")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    public ResponseEntity<PacienteDTO> buscar(@RequestParam Integer dni) throws BadRequestException, ResourceNotFoundException{
        PacienteDTO paciente = pacienteService.buscar(dni);
        return ResponseEntity.ok(paciente);
    }

    @ApiOperation(value = "Busca pacientes por nombre")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping(params = "nombre")
    public ResponseEntity<List<PacienteDTO>> buscar(@RequestParam String nombre){
        List<PacienteDTO> pacientes = pacienteService.buscar(nombre);
        return ResponseEntity.ok(pacientes);
    }

    @ApiOperation(value = "Busca pacientes por nombre y apellido")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @GetMapping(params = {"nombre", "apellido"})
    public ResponseEntity<List<PacienteDTO>> buscar(@RequestParam String nombre, @RequestParam String apellido){
        List<PacienteDTO> pacientes = pacienteService.buscar(nombre, apellido);
        return ResponseEntity.ok(pacientes);
    }

    @Override
    @ApiOperation(value = "Actualiza un paciente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PutMapping()
    public ResponseEntity<?> actualizar(@RequestBody PacienteDTO paciente) throws BadRequestException, ResourceNotFoundException {
        PacienteDTO actualizado = pacienteService.actualizar(paciente);
        return ResponseEntity.ok(actualizado);
    }

    @Override
    @ApiOperation(value = "Elimina un paciente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success | OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException {
        pacienteService.eliminar(id);
        return ResponseEntity.ok("Se eliminó correctamente el paciente con id " + id);
    }

    @Override
    @ApiOperation(value = "Busca todos los pacientes")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Bad Request") })
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> buscarTodos() {
        return ResponseEntity.ok(pacienteService.consultarTodos());
    }




}
