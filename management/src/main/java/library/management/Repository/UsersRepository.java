package library.management.Repository;

import library.management.Entites.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Usuario, Long> {
    Optional<UserDetails> findUsuarioByEmail(String username);
    Optional<UserDetails> findByEmail(String email);
    boolean existsByEmail(String email);
}
