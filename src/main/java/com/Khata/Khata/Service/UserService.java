package com.Khata.Khata.Service;

import com.Khata.Khata.Entity.User;
import com.Khata.Khata.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
    private UserRepository userRepository;

    UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User addUser(User user)
    {
        User savedUser = userRepository.save(user);
        return savedUser;
    }
    
    public User getUserById(Integer id)
    {
        return userRepository.findById(id).get();
    }
}

