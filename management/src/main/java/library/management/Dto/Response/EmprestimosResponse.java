package library.management.Dto.Response;


import library.management.enums.Status;
import java.time.LocalDate;

public record EmprestimosResponse
        (Long id,
         String aluno,
         String livro,
         LocalDate dataEmprestimo,
         LocalDate dataDevolucao,
         Status status) {
}
