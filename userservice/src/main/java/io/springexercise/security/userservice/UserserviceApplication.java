package io.springexercise.security.userservice;

import io.springexercise.security.userservice.Domain.Restaurant;
import io.springexercise.security.userservice.Domain.Role;
import io.springexercise.security.userservice.Domain.User;
import io.springexercise.security.userservice.Service.RestaurantService;
import io.springexercise.security.userservice.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();

	}


    @Bean
	CommandLineRunner run(UserService userService, RestaurantService restaurantService){
		return args -> {
		userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_RESTAURANT"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));
			userService.saveRole(new Role(null,"ROLE_HR"));


			userService.saveUser(new User(null,"Muvunyi","Muvunyi","1234",new ArrayList<>()));
			userService.saveUser(new User(null,"Gil","Will","1234",new ArrayList<>()));
			userService.saveUser(new User(null,"Millie","Tom","1234",new ArrayList<>()));
			userService.saveUser(new User(null,"LeMuguet","John","1234",new ArrayList<>()));
			userService.saveUser(new User(null,"Steven","Arnold","1234",new ArrayList<>()));

			restaurantService.save(new Restaurant(null,"Simba",5000,"Tom"));

			restaurantService.save(new Restaurant(null,"Le Muguet",5000,"John"));
			userService.addRoleToUser("Muvunyi","ROLE_ADMIN");
			userService.addRoleToUser("Will","ROLE_USER");
			userService.addRoleToUser("Tom","ROLE_RESTAURANT");
			userService.addRoleToUser("John","ROLE_RESTAURANT");



		};
	}
}
