package library.management.Controller;

import library.management.Dto.Request.CadastrarAluno;
import library.management.Dto.Request.EmprestimoRequest;
import library.management.Dto.Request.LivroRequest;
import library.management.Dto.Response.EmprestimosResponse;
import library.management.Entites.Aluno;
import library.management.Entites.Emprestimo;
import library.management.Entites.Livro;
import library.management.Services.Servico;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biblioteca")
public class Controller {

    private final Servico servico;

    public Controller(Servico servico) {
        this.servico = servico;
    }

    @PostMapping("/registraraluno")
    public ResponseEntity<Aluno> registrarAluno(@RequestBody CadastrarAluno request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.cadastrarAluno(request));
    }
    @PostMapping("/emprestimo")
    public ResponseEntity<Emprestimo> emprestimo(@RequestBody EmprestimoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.registrar(request));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Emprestimo>> listarAlunos() {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.getEmprestimos());
    }
    @PutMapping("/devolver/{id}")
    public Emprestimo devolverLivro(@PathVariable Long id) {
        return servico.devolver(id);
    }
    @GetMapping("/listaremprestimos")
    public ResponseEntity<List<EmprestimosResponse>> listarEmprestimos() {
        return ResponseEntity.status(HttpStatus.OK).body(servico.findAllEmprestimos());
    }

    @PostMapping("/registrarlivro")
    public ResponseEntity<Livro> registrarLivro(@RequestBody LivroRequest livroreq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.registrarLivro(livroreq));
    }
    @GetMapping("/filtrarativos")
    public ResponseEntity<List<EmprestimosResponse>> filtrarAtivos() {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.filtrarAtivos());
    }

    @GetMapping("/filtraratrasados")
    public ResponseEntity<List<EmprestimosResponse>> filtrarAtrasados() {
        return ResponseEntity.status(HttpStatus.CREATED).body(servico.filtrarAtrasados());
    }

    @DeleteMapping("/limparbanco")
    public String deletar(){
        servico.limpar();
        return "DB limpo";
    }
}
