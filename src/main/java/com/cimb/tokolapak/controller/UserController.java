package com.cimb.tokolapak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.cimb.tokolapak.dao.UserRepo;
import com.cimb.tokolapak.entity.User;
import com.cimb.tokolapak.util.EmailUtil;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {



    @Autowired
    private UserRepo userRepo;

    private PasswordEncoder pwEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EmailUtil emailUtil;

    @PostMapping
    public User registerUser(@RequestBody User user) {

        String encodedPassword = pwEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String confirmationLink = "<h1>Selamat!</h1>\n Anda telah bergabung bersama kami!\n Klik <a href=\"http://localhost:8080/users/userconfirmation/" +user.getEmail()+"\">link</a> ini untuk verifikasi email anda ";
        emailUtil.sendEmail(user.getEmail(), "Registrasi Karyawan", confirmationLink);

        User savedUser = userRepo.save(user);
        savedUser.setPassword(null);

        return savedUser;
    }

    @GetMapping("/userconfirmation/{email}")
    public String userConfirmation(@PathVariable String email){
        User findUserToConfirm = userRepo.findByEmail(email).get();
        findUserToConfirm.setVerified(true);
        userRepo.save(findUserToConfirm);
        return "Selamat email anda sudah berhasil dikonfirmasi";
    }





    // Cara 1 POST method
    @PostMapping("/login")
    public User loginUser (@RequestBody User user) {
        User findUser = userRepo.findByUsername(user.getUsername()).get();
        // Password raw       password sudah encode
        if (pwEncoder.matches(user.getPassword(), findUser.getPassword())) {
            findUser.setPassword(null);
            return findUser;
        }

        throw new RuntimeException("Wrong password!");
//		return null;
    }
//    Cara 2 GET
    @GetMapping("/login")
    public User getLoginUser(@RequestParam String username, @RequestParam String password) {
        User findUser = userRepo.findByUsername(username).get();

        if (pwEncoder.matches(password, findUser.getPassword())) {
            findUser.setPassword(null);
            return findUser;
        }

        throw new RuntimeException("Wrong password!");
    }

    @PostMapping("/sendEmail")
    public String sendEmailTesting() {
        this.emailUtil.sendEmail("wisnucaks@gmail.com", "Testing Spring Mail", "<h1>Hallowwww end</h1> \nWasap?");
        return "Email terkirim!";
    }
}