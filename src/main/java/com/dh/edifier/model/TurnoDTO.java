package com.dh.edifier.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TurnoDTO {

    private Integer id;
    private LocalDateTime fecha;
    private PacienteDTO paciente;
    private OdontologoDTO odontologo;

    public TurnoDTO() {
    }

    public TurnoDTO(LocalDateTime fecha, PacienteDTO paciente, OdontologoDTO odontologo) {
        this.fecha = fecha;
        this.paciente = paciente;
        this.odontologo = odontologo;
    }
}
