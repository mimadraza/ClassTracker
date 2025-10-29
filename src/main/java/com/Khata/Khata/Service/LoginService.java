package com.Khata.Khata.Service;


import com.Khata.Khata.Controller.UserController;
import com.Khata.Khata.Entity.LoginDTO;
import com.Khata.Khata.Entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private UserController userController;

    LoginService(UserController userController)
    {
        this.userController = userController;
    }
    public ResponseEntity<User> Login(LoginDTO credentials)
    {
        User user = userController.getUserByEmail(credentials.);
    }
}
