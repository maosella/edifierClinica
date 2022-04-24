package com.dh.edifier.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private Integer dni;
    private LocalDate fechaIngreso;
    private DomicilioDTO domicilio;

    public PacienteDTO() {
    }

    public PacienteDTO(String nombre, String apellido, Integer dni, LocalDate fechaIngreso, DomicilioDTO domicilio) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
    }

    public PacienteDTO(String nombre, String apellido, Integer dni, DomicilioDTO domicilio) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.domicilio = domicilio;
    }
}
