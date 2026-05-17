package com.smartcampus.events.controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcampus.events.model.User;
import com.smartcampus.events.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String requestOtp(@Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "username.exists", "There is already an account registered with that username");
            return "register";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "email.exists", "This email is already registered");
            return "register";
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_STUDENT");
        } else {
            if (!user.getRole().startsWith("ROLE_")) {
                user.setRole("ROLE_" + user.getRole());
            }
        }

        String otp = String.format("%06d", new Random().nextInt(900000) + 100000);
        session.setAttribute("pendingUser", user);
        session.setAttribute("registrationOtp", otp);
        session.setAttribute("otpExpiry", LocalDateTime.now().plusMinutes(10));
        model.addAttribute("email", user.getEmail());
        model.addAttribute("otpMessage", "An OTP has been generated and sent to your email address. For demo purposes, use " + otp + ".");

        return "register-verify";
    }

    @GetMapping("/register/verify")
    public String showOtpVerification(HttpSession session, Model model) {
        User pendingUser = (User) session.getAttribute("pendingUser");
        if (pendingUser == null) {
            return "redirect:/register";
        }
        model.addAttribute("email", pendingUser.getEmail());
        return "register-verify";
    }

    @PostMapping("/register/verify")
    public String verifyOtp(@RequestParam String otp, HttpSession session, Model model) {
        User pendingUser = (User) session.getAttribute("pendingUser");
        String expectedOtp = (String) session.getAttribute("registrationOtp");
        LocalDateTime expiry = (LocalDateTime) session.getAttribute("otpExpiry");

        if (pendingUser == null || expectedOtp == null || expiry == null) {
            return "redirect:/register";
        }

        if (LocalDateTime.now().isAfter(expiry)) {
            session.removeAttribute("pendingUser");
            session.removeAttribute("registrationOtp");
            session.removeAttribute("otpExpiry");
            model.addAttribute("otpError", "Your OTP expired. Please register again.");
            return "register";
        }

        if (!expectedOtp.equals(otp.trim())) {
            model.addAttribute("otpError", "Incorrect OTP. Please try again.");
            model.addAttribute("email", pendingUser.getEmail());
            return "register-verify";
        }

        pendingUser.setPassword(passwordEncoder.encode(pendingUser.getPassword()));
        userRepository.save(pendingUser);
        session.removeAttribute("pendingUser");
        session.removeAttribute("registrationOtp");
        session.removeAttribute("otpExpiry");

        return "redirect:/login?success";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/events";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
            return "redirect:/student/events";
        }
        return "redirect:/";
    }

    @GetMapping("/implementation-steps")
    public String implementationSteps() {
        return "implementation-steps";
    }

    @GetMapping("/login/otp")
    public String showLoginOtp(HttpSession session, Model model) {
        Boolean otpVerified = (Boolean) session.getAttribute("loginOtpVerified");
        if (otpVerified != null && otpVerified) {
            return "redirect:/dashboard";
        }
        
        String otp = (String) session.getAttribute("loginOtp");
        if (otp == null) {
            otp = String.format("%06d", new Random().nextInt(900000) + 100000);
            session.setAttribute("loginOtp", otp);
        }
        model.addAttribute("otpMessage", "An OTP has been sent for login. For demo purposes, use " + otp + ".");
        return "login-otp";
    }

    @PostMapping("/login/otp")
    public String verifyLoginOtp(@RequestParam String otp, HttpSession session, Model model) {
        String expectedOtp = (String) session.getAttribute("loginOtp");
        if (expectedOtp != null && expectedOtp.equals(otp.trim())) {
            session.setAttribute("loginOtpVerified", true);
            return "redirect:/dashboard";
        }
        model.addAttribute("otpError", "Invalid OTP. Please try again.");
        String expected = (String) session.getAttribute("loginOtp");
        model.addAttribute("otpMessage", "An OTP has been sent for login. For demo purposes, use " + expected + ".");
        return "login-otp";
    }
}
