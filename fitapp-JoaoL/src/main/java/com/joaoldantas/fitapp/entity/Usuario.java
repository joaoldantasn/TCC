package com.joaoldantas.fitapp.entity;

import com.joaoldantas.fitapp.util.UsuarioRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String telefone;

    private Double altura;

    private Double peso;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Treino> treinos;

//    @ManyToMany
//    @JoinTable(name = "tb_usuario_desafio",
//            joinColumns = @JoinColumn(name = "usuario_id"),
//            inverseJoinColumns = @JoinColumn(name = "desafio_id"))
//    private List<Desafio> desafios;

    @ManyToMany(mappedBy = "participantes", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    private List<Desafio> desafios;

    @OneToMany(mappedBy = "desafioCriador")
    private List<Desafio> desafiosCriados;

    private String FotoPerfil;

    // campos JWT
    @Column(length = 80)
    private String senha;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private UsuarioRole role;

}
