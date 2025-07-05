package com.classtrackeer.classtracker.Repository

import com.classtrackeer.classtracker.Model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int>{
}