package library.management.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "livros")
@Getter
@Setter
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String autor;

    private String editora;

    private String descricao;

    @NotNull
    @Min(0)
    private Integer quantidade;

    public boolean ativo(){
        return quantidade != null && quantidade > 0;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "livro")
    private List<Emprestimo> emprestimos;
}
