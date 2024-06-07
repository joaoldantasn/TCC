package com.joaoldantas.fitapp.repository;

import com.joaoldantas.fitapp.entity.Desafio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesafioRepository extends JpaRepository<Desafio, Long> {

    Desafio findByNome(String nome);

    @Query("SELECT d FROM Desafio d WHERE d NOT IN (SELECT dc FROM Desafio dc JOIN dc.participantes u WHERE u.email = :userEmail)")
    List<Desafio> findDesafiosUsuarioNaoParticipando(@Param("userEmail") String userEmail);
}
