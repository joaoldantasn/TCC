package com.joaoldantas.fitapp.entity;

import com.joaoldantas.fitapp.util.TipoDesafio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tb_desafio")
public class Desafio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String descricao;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDate startAt;

    private LocalDate endAt;

    private String premiacao;

    private String imagem;

    @Enumerated(EnumType.STRING)
    private TipoDesafio tipo;

    @OneToMany(mappedBy = "desafio", cascade = CascadeType.ALL)
    private List<Treino> treinos;

    //    @ManyToMany(mappedBy = "desafios", cascade = CascadeType.ALL)
//    private List<Usuario> participantes;
    @ManyToMany
    @JoinTable(
            name = "tb_desafio_usuarios",
            joinColumns = @JoinColumn(name = "desafio_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> participantes;

    @ManyToOne
    @JoinColumn(name = "usuario_criador_id")
    private Usuario desafioCriador;

    private boolean finalizado;

}
