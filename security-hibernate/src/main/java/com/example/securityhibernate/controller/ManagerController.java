package com.example.securityhibernate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {

    @GetMapping("")
    public ResponseEntity<?> manager() {
        return new ResponseEntity<>("Hello manager", HttpStatus.OK);
    }

}
