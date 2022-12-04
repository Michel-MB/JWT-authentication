package io.springexercise.security.userservice.Service;

import io.springexercise.security.userservice.Domain.Restaurant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface RestaurantService {
    Restaurant save(Restaurant rest);
    Restaurant getRest(String username);

}
