package com.smartcampus.events.controller;

import com.smartcampus.events.model.Registration;
import com.smartcampus.events.model.User;
import com.smartcampus.events.repository.RegistrationRepository;
import com.smartcampus.events.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping("/check")
    public Map<String, Object> checkAlarms(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("alert", false);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return response;
        }

        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (user == null) return response;

        List<Registration> registrations = registrationRepository.findByUser(user);
        LocalDateTime now = LocalDateTime.now();

        for (Registration reg : registrations) {
            LocalDateTime eventTime = reg.getEvent().getEventDate();
            
            // If the event starts in exactly 0 to 5 minutes
            long minutesUntil = ChronoUnit.MINUTES.between(now, eventTime);
            if (minutesUntil >= 0 && minutesUntil <= 5) {
                response.put("alert", true);
                response.put("message", "⏰ ALARM! Your event '" + reg.getEvent().getTitle() + "' is starting in less than 5 minutes at " + reg.getEvent().getLocation() + "!");
                // Just trigger for the first one found
                break;
            }
        }
        
        return response;
    }
}
