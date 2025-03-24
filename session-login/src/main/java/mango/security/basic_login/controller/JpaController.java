package mango.security.basic_login.controller;

import lombok.RequiredArgsConstructor;
import mango.security.basic_login.service.JpaUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/jpa")
@RequiredArgsConstructor
public class JpaController {
    private final JpaUserDetailsService jpaUserDetailsService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "jpa");
        return "login";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("loginType", "jpa");
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            jpaUserDetailsService.addUser(username, password);
            return "redirect:/jpa/login";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String logoutPage(Model model) {
        model.addAttribute("loginType", "jpa");
        return "logout"; // logout.html을 렌더링
    }
}