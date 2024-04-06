package com.example.securityhibernate.controller;

import com.example.securityhibernate.service.imp.UsersServiceImp;
import com.example.securityhibernate.utils.JwtUtilsHelpers;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/web")
public class WebController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilsHelpers jwtUtilsHelpers;

    @Autowired
    private UsersServiceImp userServiceImp;

    @GetMapping("/home")
    public String homepage() {
        return "home";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestParam String username,
                                   @RequestParam String password) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        Gson gson = new Gson();
        String data = gson.toJson(authentication);

        System.out.println("Data: "+data);

        return new ResponseEntity<>(jwtUtilsHelpers.generateToken(data, username), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam String role) {
        if (userServiceImp.addUser(username, password, role)) {
            return new ResponseEntity<>("Them thanh cong", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Them that bai", HttpStatus.OK);
        }
    }

}