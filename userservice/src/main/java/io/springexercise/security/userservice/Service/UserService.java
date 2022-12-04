package io.springexercise.security.userservice.Service;

import io.springexercise.security.userservice.Domain.Role;
import io.springexercise.security.userservice.Domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);

    User  getUser(String username);

    void addRoleToUser(String username,String roleName);
    List<User> getUsers();


}
