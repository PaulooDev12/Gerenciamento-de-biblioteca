package library.management.Repository;

import library.management.Entites.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunosRepository extends JpaRepository<Aluno, Long> {
    Aluno findByNomeIgnoreCase(String nome);
}
