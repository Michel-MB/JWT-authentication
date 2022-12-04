package io.springexercise.security.userservice.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.springexercise.security.userservice.Domain.Role;
import io.springexercise.security.userservice.Domain.User;
import io.springexercise.security.userservice.Exception.GeneralException;
import io.springexercise.security.userservice.Repo.RoleRepo;
import io.springexercise.security.userservice.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService , UserDetailsService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepo.getByUsername(username);
        if(user == null){
            log.info("Error User not Found");
            throw new UsernameNotFoundException("User not Found in Database");

        }
        else{
            log.info("User found");

        }
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
       user.getRole().forEach(role -> {
           authorities.add(new SimpleGrantedAuthority(role.getName()));
       });
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new User",user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);


    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new Role",role.getName());
        return roleRepo.save(role);
    }

    @Override
    public User getUser(String username) {
        return userRepo.getByUsername(username);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        log.info("Saving role to User" ,username ,roleName);
        User user=userRepo.getByUsername(username);
        Role role=roleRepo.getByname(roleName);
        user.getRole().add(role);
    }

    @Override
    public List<User> getUsers() {
        log.info("fetching all users");
        return userRepo.findAll();
    }
    protected User checkLoginUser(String username, String password) {
        User user = getUser(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new GeneralException("wrong password!", HttpStatus.NOT_FOUND);
        }
        return user;
    }

}
