package io.springexercise.security.userservice.Repo;

import io.springexercise.security.userservice.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role getByname(String name);
}
