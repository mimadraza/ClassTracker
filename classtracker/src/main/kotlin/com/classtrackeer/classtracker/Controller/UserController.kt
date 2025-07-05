package com.classtrackeer.classtracker.Controller

import com.classtrackeer.classtracker.Model.User
import com.classtrackeer.classtracker.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user")
class UserController (@Autowired val userService : UserService){

    @PostMapping("/addUser")
    fun addUser(@RequestBody user : User) : ResponseEntity<User> {
        val savedUser : User = userService.addUser(user)
        return ResponseEntity(savedUser, HttpStatus.OK)
    }
}