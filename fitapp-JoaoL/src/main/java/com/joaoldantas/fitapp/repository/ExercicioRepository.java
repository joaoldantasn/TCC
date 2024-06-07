package com.joaoldantas.fitapp.repository;

import com.joaoldantas.fitapp.entity.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    Exercicio findByNome(String nome);
}
