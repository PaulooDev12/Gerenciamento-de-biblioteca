package library.management.Repository;

import library.management.Entites.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivrosRepository extends JpaRepository<Livro, Long> {
    Livro findByTituloIgnoreCase(String nome);
    boolean existsByTituloIgnoreCase(String nome);
}