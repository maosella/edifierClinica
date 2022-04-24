package com.dh.edifier.model;

import lombok.Data;

@Data
public class OdontologoDTO {

    private Integer id;
    private Integer dni;
    private String nombre;
    private String apellido;
    private Integer matricula;

    public OdontologoDTO() {
    }

    public OdontologoDTO(Integer dni, String nombre, String apellido, Integer matricula) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.matricula = matricula;
    }
}
