package io.springexercise.security.userservice.Service;



import io.springexercise.security.userservice.Domain.RefreshToken;
import io.springexercise.security.userservice.Domain.User;
import io.springexercise.security.userservice.Exception.GeneralException;
import io.springexercise.security.userservice.Repo.RefreshTokenRepository;
import io.springexercise.security.userservice.Request.LoginRequest;
import io.springexercise.security.userservice.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthService {

    @Value("${jwt-variables.EXPIRES_REFRESH_TOKEN_MINUTE}")
    private String EXPIRES_REFRESH_TOKEN_MINUTE;

    private final UserServiceImpl userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtHelper;

    public AuthService(UserServiceImpl userService,
                       RefreshTokenRepository refreshTokenRepository,
                       JwtUtil jwtHelper) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtHelper = jwtHelper;
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        String token;
        do {

            token = jwtHelper.generateRefreshToken();

        } while (refreshTokenRepository.findByToken(token).isPresent());

        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(LocalDateTime.now().plusMinutes(Long.parseLong(EXPIRES_REFRESH_TOKEN_MINUTE)));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }


    public Map<String, String> createTokens(User user) {
        return createTokens(user.getUsername());
    }

    public Map<String, String> createTokens(String username) {

        var user = userService.getUser(username);

        String access_token = jwtHelper.createJwtToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        Map<String, String> map = new HashMap();
        map.put("access_token", access_token);
        map.put("refresh_token", refreshToken.getToken());
        map.put("username", user.getUsername());
        map.put("id", user.getId().toString());
        return map;
    }


//    public Map<String, String> registerUser(CreateUserRequest createUserRequest) {
//
//        var user = User.builder()
//                .username(createUserRequest.getUsername())
//                .email(createUserRequest.getEmail())
//                .password(createUserRequest.getPassword())
//                .build();
//        userService.saveUser(user);
//        return createTokens(user);
//
//    }


    public Map<String, String> login(LoginRequest loginRequest) {
        deleteOldTokensByLogin(loginRequest.getUsername());
        var user = userService.checkLoginUser(loginRequest.getUsername(), loginRequest.getPassword());
        return createTokens(user);
    }

    public void logout() {
        deleteOldTokensByUsername(getAuthenticatedUser().getUsername());
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUser(auth.getName());
    }

    public UsernamePasswordAuthenticationToken getAuthToken(String token) {
        return jwtHelper.getAuthToken(token);
    }


    public Map<String, String> refreshTokens(String refreshToken) {

        RefreshToken token = getRefreshToken(refreshToken);

        if (token != null) {
            deleteRefreshToken(token.getToken());

            return createTokens(token.getUser());
        } else
            throw new GeneralException("Invalid refresh token!", HttpStatus.BAD_REQUEST);

    }

    public void deleteRefreshToken(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        refreshTokenRepository.delete(refreshToken);
    }

    public void deleteOldTokensByLogin(String username) {
        refreshTokenRepository.deleteRefreshTokenByUser(userService.getUser(username));
    }

    public void deleteOldTokensByUsername(String username) {
        User user = userService.getUser(username);

        if (refreshTokenRepository.findByUser(user).isPresent()) {
            refreshTokenRepository.deleteRefreshTokenByUser(user);
        } else {
            throw new GeneralException("you already have been logouted!", HttpStatus.BAD_REQUEST);
        }

    }


    protected RefreshToken getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new GeneralException("token not found", HttpStatus.NOT_FOUND));
    }


}
