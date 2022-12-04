package io.springexercise.security.userservice.Repo;

import io.springexercise.security.userservice.Domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepo extends JpaRepository<Restaurant, Long> {
    Restaurant getByUsername(String username);
}
