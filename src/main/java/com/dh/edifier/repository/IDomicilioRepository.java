package com.dh.edifier.repository;

import com.dh.edifier.entities.Domicilio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface IDomicilioRepository extends JpaRepository<Domicilio, Integer> {

    @Query("SELECT d FROM Domicilio d WHERE d.calle=?1")
    Optional<List<Domicilio>> buscar(String calle);

    @Query("SELECT d FROM Domicilio d WHERE d.calle=?1 AND d.numero=?2")
    Optional<List<Domicilio>> buscar(String calle, Integer numero);

    @Query("SELECT d FROM Domicilio d WHERE d.calle=?1 AND d.numero=?2")
    Optional<Domicilio> buscar(String calle, Integer numero, String localidad, String provincia);

}
