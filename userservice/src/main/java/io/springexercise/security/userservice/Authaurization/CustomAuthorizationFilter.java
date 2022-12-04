package io.springexercise.security.userservice.Authaurization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.springexercise.security.userservice.Service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public CustomAuthorizationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login")) {
            filterChain.doFilter(request, response);
        }
        else {
           String authorizationHeader=request.getHeader(AUTHORIZATION);
           if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){

                  String token = authorizationHeader.substring("Bearer ".length());
//                  Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//                  JWTVerifier verifier = JWT.require(algorithm).build();
//                  DecodedJWT decodeJWT = verifier.verify(token);
//                  String username = decodeJWT.getSubject();
//                  String[] roles = decodeJWT.getClaim("roles").asArray(String.class);
//                  Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                  stream(roles).forEach(role -> {
//                      authorities.add(new SimpleGrantedAuthority(role));
//                          }

                  try {
                      UsernamePasswordAuthenticationToken authToken;
                      authToken = authService.getAuthToken(token);
                      SecurityContextHolder.getContext().setAuthentication(authToken);
                      filterChain.doFilter(request, response);
                  }

              catch (Exception exception){
                   log.error("Error logging in :{}", exception.getMessage());
                   response.setHeader("error",exception.getMessage());
                   response.setStatus(FORBIDDEN.value());
//                   response.sendError(FORBIDDEN.value());
                  Map<String, String> error= new HashMap<>();
                  error.put("error_message", exception.getMessage());

                  response.setContentType(APPLICATION_JSON_VALUE);
                  new ObjectMapper().writeValue(response.getOutputStream(),error);

              }
              }
           else{
               filterChain.doFilter(request,response);
           }
        }
    }
}
