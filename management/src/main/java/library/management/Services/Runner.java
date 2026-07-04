package library.management.Services;

import library.management.Entites.Usuario;
import library.management.Repository.UsersRepository;
import library.management.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@Order(1)
public class Runner implements CommandLineRunner {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    public Runner(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }
    private static final Logger logger = Logger.getLogger(Runner.class.getName());

    @Value("${admin.password}")
    private String password;

    @Value("${admin.email}")
    private String email;


    @Override
    public void run(String... args) throws Exception {

        String username = "admin";
        if(!usersRepository.existsByEmail(email)){
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setUsername(username);
            usuario.setRole(Role.valueOf("ROLE_ADMIN"));
            usersRepository.save(usuario);
            logger.info("admin adicionado com sucesso");
        }else{
            logger.info("Admin ja existe");
        }
    }
}