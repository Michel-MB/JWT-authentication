package io.springexercise.security.userservice.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.springexercise.security.userservice.Domain.Role;
import io.springexercise.security.userservice.Domain.User;
import io.springexercise.security.userservice.Exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
@Component
public class JwtUtil {
    @Value("${jwt-variables.KEY}")
    private String KEY;

    @Value("${jwt-variables.ISSUER}")
    private String ISSUER;

    @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN_MINUTE}")
    private Integer EXPIRES_ACCESS_TOKEN_MINUTE;


    public String createJwtToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());

        List<String> roles = user.getRole().stream().map(Role::getName).collect(Collectors.toList());

        return JWT.create().withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (EXPIRES_ACCESS_TOKEN_MINUTE * 60 * 1000)))
                .withIssuedAt(convertToDateViaSqlDate(LocalDate.now()))
                .withIssuer(ISSUER)
                .withClaim("roles", roles).sign(algorithm);
    }


    public String generateRefreshToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    public UsernamePasswordAuthenticationToken getAuthToken(String token) {

        DecodedJWT decodedJWT = verifyJWT(token);
        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);

    }

    public DecodedJWT verifyJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(EXPIRES_ACCESS_TOKEN_MINUTE * 60).build();

        try {
            return verifier.verify(token);
        } catch (Exception e) {
            throw new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

}
