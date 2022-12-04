package io.springexercise.security.userservice.Repo;

import io.springexercise.security.userservice.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User getByUsername(String username);

}
