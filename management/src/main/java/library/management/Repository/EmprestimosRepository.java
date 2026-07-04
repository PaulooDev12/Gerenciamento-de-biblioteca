package library.management.Repository;

import library.management.Entites.Emprestimo;
import library.management.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmprestimosRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByStatus(Status status);
}
