package com.Khata.Khata.Controller;

import com.Khata.Khata.Entity.User;
import com.Khata.Khata.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class UserController
{
    private final UserService userService;

    UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping
    public User me(Authentication auth)
    {
        return userService.getUserById((Integer) auth.getPrincipal());
    }
}
