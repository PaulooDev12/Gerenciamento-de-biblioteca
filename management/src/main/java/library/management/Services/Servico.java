package library.management.Services;

import library.management.Dto.Mapper.Mapper;
import library.management.Dto.Request.CadastrarAluno;
import library.management.Dto.Request.EmprestimoRequest;
import library.management.Dto.Request.LivroRequest;
import library.management.Dto.Response.AlunoResponse;
import library.management.Dto.Response.EmprestimosResponse;
import library.management.Entites.Aluno;
import library.management.Entites.Emprestimo;
import library.management.Entites.Livro;
import library.management.Repository.AlunosRepository;
import library.management.Repository.EmprestimosRepository;
import library.management.Repository.LivrosRepository;
import library.management.enums.Status;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class Servico {

    private final AlunosRepository alunosRepository;
    private final EmprestimosRepository emprestimosRepository;
    private final LivrosRepository livrosRepository;

    public Servico(AlunosRepository alunosRepository, EmprestimosRepository emprestimosRepository, LivrosRepository livrosRepository) {
        this.alunosRepository = alunosRepository;
        this.emprestimosRepository = emprestimosRepository;
        this.livrosRepository = livrosRepository;
    }

    public Aluno cadastrarAluno(CadastrarAluno request) {
        Aluno aluno = new Aluno();
        aluno.setNome(request.nome());
        aluno.setTurma(request.turma());
        aluno.setMatricula(request.matricula());

        return alunosRepository.save(aluno);

    }

    public Emprestimo registrar(EmprestimoRequest request) {
        Aluno aluno = alunosRepository.findById(request.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno nao encontrado"));

        if(!livrosRepository.existsByTituloIgnoreCase(request.livroTitulo())) {
            throw new RuntimeException("Livro nao encontrado");
        }
        Livro livro = livrosRepository.findByTituloIgnoreCase(request.livroTitulo());


        if (!livro.ativo()){
            throw new RuntimeException("Livro nao disponivel");
        }

        Emprestimo emprestimo = new Emprestimo();

        LocalDate hoje = LocalDate.now();
        LocalDate devolucao = LocalDate.now().plusDays(request.duracao());
        emprestimo.setLivro(livro);
        emprestimo.setDataemprestimo(hoje);
        emprestimo.setDevolucao(devolucao);
        emprestimo.setStatus(Status.ATIVO);
        livro.setQuantidade(livro.getQuantidade() - 1);
        emprestimo.setAluno(aluno);

        livrosRepository.save(livro);


        return emprestimosRepository.save(emprestimo);
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimosRepository.findAll();
    }

    public List<AlunoResponse> findAll(){
        List<Aluno> alunos = alunosRepository.findAll();

        return alunos.stream()
                .map(Mapper::map)
                .toList();
    }
    public List<EmprestimosResponse> findAllEmprestimos(){
        List<Emprestimo> emprestimos = emprestimosRepository.findAll();

        for(Emprestimo emprestimo : emprestimos){
            if(emprestimo.getStatus() == (Status.ATIVO) && (LocalDate.now().equals(emprestimo.getDevolucao()) || LocalDate.now().isAfter(emprestimo.getDataemprestimo()))) {
                emprestimo.setStatus(Status.ATRASADO);

                emprestimosRepository.save(emprestimo);

            }
        }
        return emprestimos.stream()
                .map(Mapper::toResponse)
                .toList();
    }
    public Emprestimo devolver(Long id){
        Emprestimo emprestimo = emprestimosRepository.findById(id).orElseThrow(() -> new RuntimeException("Emprestimo nao encontrado"));
        emprestimo.setStatus(Status.DEVOLIDO);
        Livro livro = emprestimo.getLivro();
        livro.setQuantidade(livro.getQuantidade() + 1);
        livrosRepository.save(livro);
        return emprestimosRepository.save(emprestimo);
    }
    public void limpar(){
        emprestimosRepository.deleteAll();
        livrosRepository.deleteAll();
        alunosRepository.deleteAll();
    }
    public List<EmprestimosResponse> filtrarAtrasados(){
        List<Emprestimo> atrasados = emprestimosRepository.findByStatus(Status.ATRASADO);
        return atrasados.stream()
                .map(Mapper::toResponse)
                .toList();
    }
    public List<EmprestimosResponse> filtrarAtivos(){
        List<Emprestimo> ativos = emprestimosRepository.findByStatus(Status.ATIVO);
        return ativos.stream()
                .map(Mapper::toResponse)
                .toList();
    }

    public Livro registrarLivro(LivroRequest request){
        Livro livro = new Livro();
        livro.setTitulo(request.titulo());
        livro.setAutor(request.autor());
        livro.setEditora(request.editora());
        livro.setDescricao(request.descricao());
        livro.setQuantidade(request.quantidade());

        return livrosRepository.save(livro);
    }

}
