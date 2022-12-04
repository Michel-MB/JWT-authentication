package io.springexercise.security.userservice.Service;

import io.springexercise.security.userservice.Domain.Restaurant;
import io.springexercise.security.userservice.Repo.RestaurantRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RestServiceImpl  implements RestaurantService{
    private final RestaurantRepo restaurantRepo;

    @Override
    public Restaurant save(Restaurant rest) {
        return restaurantRepo.save(rest);
    }

    @Override
    public Restaurant getRest(String name) {
        return restaurantRepo.getByUsername(name);
    }
}
