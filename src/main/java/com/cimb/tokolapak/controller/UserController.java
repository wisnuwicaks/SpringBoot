package com.cimb.tokolapak.controller;

import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserRepo userRepo;

    private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @PostMapping
    public User registerUser(@RequestBody User user) {

//        Optional<User> findUser = userRepo.findByUsername(user.getUsername());
//
//        if (findUser.toString() != "Optional.empty") {
//            throw new RuntimeException("Username exists!");
//        }
        String encodedPassword = pwEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    //Cara 1 login menggunakan post method
    @PostMapping("/login")
    public User loginUser(@RequestBody User user) {
        User findUser = userRepo.findByUsername(user.getUsername()).get();
        if (pwEncoder.matches(user.getPassword(), findUser.getPassword())) {
            return findUser;
        }
        return null;
    }

    // localhost:8080/users/login?username=seto&password=password123
    @GetMapping("/login")
    public User getLoginUser(@RequestParam String username, @RequestParam String password) {
        User findUser = userRepo.findByUsername(username).get();

        if (pwEncoder.matches(password, findUser.getPassword())) {
            findUser.setPassword(null);
            return findUser;
        }

        throw new RuntimeException("Wrong password!");
    }


}
