package com.smartcampus.events.controller;

import com.smartcampus.events.model.User;
import com.smartcampus.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String showProfile(Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication authentication, 
                                @RequestParam String name, 
                                @RequestParam String email, 
                                @RequestParam String department,
                                @RequestParam(required = false) String password) {
        
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        user.setName(name);
        user.setEmail(email);
        user.setDepartment(department);
        
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        
        userRepository.save(user);
        return "redirect:/profile?success_profile=true";
    }
}
