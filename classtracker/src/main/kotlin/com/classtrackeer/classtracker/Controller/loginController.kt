package com.classtrackeer.classtracker.Controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class loginController {

    @GetMapping("/login")
    fun showLoginPage(): String {
        return "login"
    }
    @GetMapping("/signup")
    fun showSignupPage() : String {
        return "signup"
    }
    @GetMapping("/dashboard")
    fun showDashboard() : String {
        return "dashboard"
    }
}