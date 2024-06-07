package com.joaoldantas.fitapp.repository;

import com.joaoldantas.fitapp.entity.Treino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreinoRepository extends JpaRepository<Treino, Long> {
}
