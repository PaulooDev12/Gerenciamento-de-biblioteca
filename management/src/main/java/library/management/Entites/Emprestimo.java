package library.management.Entites;

import jakarta.persistence.*;
import library.management.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "emprestimos")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataemprestimo;
    private LocalDate devolucao;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "livro_titulo")
    private Livro livro;


}