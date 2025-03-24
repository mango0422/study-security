package mango.security.basic_login.controller;

import lombok.RequiredArgsConstructor;
import mango.security.basic_login.service.InMemoryUserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inmemory")
@RequiredArgsConstructor
public class    InMemoryController {
    private final InMemoryUserDetailsService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "inmemory");
        return "login";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("loginType", "inmemory");
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            userService.addUser(username, password, "USER");
            return "redirect:login";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/logout")
    public String logoutPage(Model model) {
        model.addAttribute("loginType", "inmemory");
        return "logout"; // logout.html을 렌더링
    }

}
