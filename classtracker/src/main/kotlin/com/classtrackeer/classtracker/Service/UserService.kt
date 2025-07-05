package com.classtrackeer.classtracker.Service

import com.classtrackeer.classtracker.Model.User
import com.classtrackeer.classtracker.Repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService (@Autowired val userRepository : UserRepository){

    fun addUser(user : User) : User {
        return userRepository.save(user)
    }
}