package io.springexercise.security.userservice.Repo;


import io.springexercise.security.userservice.Domain.RefreshToken;
import io.springexercise.security.userservice.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    void deleteRefreshTokenByUser(User user);

}
