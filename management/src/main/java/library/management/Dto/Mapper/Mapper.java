package library.management.Dto.Mapper;


import library.management.Dto.Response.AlunoResponse;
import library.management.Dto.Response.EmprestimosResponse;
import library.management.Entites.Aluno;
import library.management.Entites.Emprestimo;

public class Mapper {
    public static AlunoResponse map(Aluno aluno){
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getTurma(),
                aluno.getId()
        );

    }
    public static EmprestimosResponse toResponse(Emprestimo emprestimo){
        return new EmprestimosResponse(
                emprestimo.getId(),
                emprestimo.getAluno().getNome(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataemprestimo(),
                emprestimo.getDevolucao(),
                emprestimo.getStatus()
        );
    }
}
