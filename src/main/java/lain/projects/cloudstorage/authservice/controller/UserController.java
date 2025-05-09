package lain.projects.cloudstorage.authservice.controller;

import lain.projects.cloudstorage.authservice.dto.UserResponse;
import lain.projects.cloudstorage.model.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetailsImpl user){
        return ResponseEntity.ok(new UserResponse(user.getUsername()));
    }
}
