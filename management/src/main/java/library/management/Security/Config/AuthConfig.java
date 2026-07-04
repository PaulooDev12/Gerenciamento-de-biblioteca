package library.management.Security.Config;

import library.management.Repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthConfig implements UserDetailsService {

    private final UsersRepository usersRepository;

    public AuthConfig(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findUsuarioByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}

