package library.management.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Long matricula;
    private String turma;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno")
    private List<Emprestimo> emprestimos;

}
