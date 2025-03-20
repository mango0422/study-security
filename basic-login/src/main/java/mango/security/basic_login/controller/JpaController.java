package mango.security.basic_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpa")
public class JpaController {

    @GetMapping("/login")
    public String loginPage() {
        return "jpa/login";
    }

    @GetMapping("/home")
    public String homePage() {
        return "jpa/home";
    }
}