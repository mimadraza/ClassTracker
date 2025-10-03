package com.Khata.Khata.Controller;

import com.Khata.Khata.Entity.User;
import com.Khata.Khata.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/User")
public class UserController
{
    private UserService userService;

    UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user)
    {
        User savedUser = userService.addUser(user);
        return ResponseEntity.ok(savedUser);
    }
}

