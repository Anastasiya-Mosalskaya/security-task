package org.epam.securitytask.controller;

import org.epam.securitytask.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SecurityController {

    @Autowired
    public LoginAttemptService loginAttemptService;

    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/info")
    public String info(){
        return "info";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<String> blockedUsers = loginAttemptService.getBlockedUsers();
        model.addAttribute("blockedUsers", blockedUsers);
        return "admin";
    }
}
