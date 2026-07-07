package library.management.Controller;

import library.management.Dto.Request.CadastrarUsuario;
import library.management.Dto.Request.LoginRequest;
import library.management.Dto.Response.RegisterResponse;
import library.management.Entites.Usuario;
import library.management.Repository.UsersRepository;
import library.management.Security.Jwt.JwtProvider;
import library.management.enums.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtProvider jwtProvider, UsersRepository usersRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.usersRepository = usersRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody CadastrarUsuario cadastrarUsuario) {
        Usuario usuario = new Usuario();
        usuario.setUsername(cadastrarUsuario.username());
        usuario.setEmail(cadastrarUsuario.email());
        usuario.setPassword(passwordEncoder.encode(cadastrarUsuario.password()));
        usuario.setRole(Role.ROLE_USER);
        usersRepository.save(usuario);
        return ResponseEntity.ok(new RegisterResponse(cadastrarUsuario.username(),  cadastrarUsuario.email()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Usuario usuario = (Usuario) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtProvider.generateJwtCookie(usuario);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body("Login realizado com sucesso!");
    }

}

