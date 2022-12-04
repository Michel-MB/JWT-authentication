package io.springexercise.security.userservice.controler;


import io.springexercise.security.userservice.Request.LoginRequest;
import io.springexercise.security.userservice.Request.RefreshTokenRequest;
import io.springexercise.security.userservice.Service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;



    public AuthController(AuthService authService) {
        this.authService = authService;

    }


//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
//        return ResponseEntity.ok().body(authService.registerUser(createUserRequest));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok().body(authService.refreshTokens(refreshTokenRequest.getToken()));
    }


}
