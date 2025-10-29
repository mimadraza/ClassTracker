package com.Khata.Khata.Controller;

import com.Khata.Khata.Entity.LoginDTO;
import com.Khata.Khata.Entity.User;
import com.Khata.Khata.Service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class LoginController {

    private LoginService loginService;

    LoginController(LoginService loginService)
    {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public User Login(@RequestBody LoginDTO credentials)
    {

    }
}
