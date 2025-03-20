package mango.security.basic_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inmemory")
public class InMemoryController {

    @GetMapping("/login")
    public String loginPage() {
        return "inmemory/login";
    }

    @GetMapping("/home")
    public String homePage() {
        return "inmemory/home";
    }
}
