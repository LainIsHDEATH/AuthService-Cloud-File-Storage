package lain.projects.authservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lain.projects.authservice.dto.SignInRequest;
import lain.projects.authservice.dto.SignUpRequest;
import lain.projects.authservice.dto.UserResponse;
import lain.projects.authservice.model.User;
import lain.projects.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> register (@RequestBody @Valid SignUpRequest dto){
        User user = userService.register(dto.username(), dto.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.getUsername()));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserResponse> login (@RequestBody @Valid SignInRequest dto,
                                               HttpServletRequest request){
        User user = userService.login(dto.username(), dto.password());

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);

        request.getSession(true).setAttribute(
                SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        return ResponseEntity.ok(new UserResponse(user.getUsername()));
    }
}
