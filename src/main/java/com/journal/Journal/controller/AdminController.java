package com.journal.Journal.controller;

import com.journal.Journal.entity.User;
import com.journal.Journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
            List<User> users  = userService.getAllUsers();
            if(users!=null)
                return new ResponseEntity<>(users, HttpStatus.OK);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
