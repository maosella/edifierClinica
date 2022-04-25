package com.dh.edifier.controller;

import com.dh.edifier.model.TurnoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ITurnoController extends CRUDController<TurnoDTO> {
    ResponseEntity<List<TurnoDTO>> buscarTurnosDesde(@RequestParam(value = "dd") Integer dia,
                                                     @RequestParam(value = "mm") Integer mes,
                                                     @RequestParam(value = "yyyy") Integer anio,
                                                     @RequestParam(value = "hh") Integer horas,
                                                     @RequestParam(value = "ss") Integer segundos,
                                                     @RequestParam(value = "cantDias") Integer cantidadDias);
}
