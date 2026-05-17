package com.smartcampus.events.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.smartcampus.events.model.Event;
import com.smartcampus.events.model.Registration;
import com.smartcampus.events.model.User;
import com.smartcampus.events.repository.EventRepository;
import com.smartcampus.events.repository.RegistrationRepository;
import com.smartcampus.events.repository.UserRepository;

@Controller
@RequestMapping("/student/events")
public class StudentEventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String viewUpcomingEvents(@RequestParam(required = false) String search, Model model, Authentication authentication) {
        List<Event> upcomingEvents;
        
        if (search != null && !search.trim().isEmpty()) {
            upcomingEvents = eventRepository.searchEvents(null, null, search);
            // student page only shows future events
            upcomingEvents = upcomingEvents.stream()
                .filter(e -> e.getEventDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
        } else {
            upcomingEvents = eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDateTime.now());
        }
        
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        
        if (user != null) {
            List<Registration> userRegs = registrationRepository.findByUser(user);
            List<Long> registeredEventIds = userRegs.stream().map(r -> r.getEvent().getId()).collect(Collectors.toList());
            model.addAttribute("registeredEventIds", registeredEventIds);
        }
        
        // Decorate with capacity
        upcomingEvents.forEach(e -> e.setCurrentRegistrations((int)registrationRepository.countByEvent(e)));
        
        model.addAttribute("events", upcomingEvents);
        model.addAttribute("search", search);
        return "student/events-list";
    }

    @PostMapping("/{id}/register")
    public String registerForEvent(@PathVariable long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        
        if (registrationRepository.existsByUserAndEvent(user, event)) {
            redirectAttributes.addFlashAttribute("error", "You are already registered for this event.");
            return "redirect:/student/events";
        }
        
        int currentRegs = (int) registrationRepository.countByEvent(event);
        if (currentRegs >= event.getMaxCapacity()) {
            redirectAttributes.addFlashAttribute("error", "Sorry, this event is already full.");
            return "redirect:/student/events";
        }

        Registration registration = new Registration(user, event);
        registrationRepository.save(registration);
        
        redirectAttributes.addFlashAttribute("success", "Successfully registered for " + event.getTitle());
        return "redirect:/student/events/my-events";
    }

    @GetMapping("/{id}")
    public String eventDetails(@PathVariable long id, Model model, Authentication authentication) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        boolean isRegistered = registrationRepository.existsByUserAndEvent(user, event);
        int currentRegistrations = (int) registrationRepository.countByEvent(event);
        event.setCurrentRegistrations(currentRegistrations);

        model.addAttribute("event", event);
        model.addAttribute("isRegistered", isRegistered);
        model.addAttribute("mapEnabled", event.getLocation() != null && !event.getLocation().trim().isEmpty());
        return "student/event-details";
    }

    @GetMapping("/my-events")
    public String myRegistrations(Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        List<Registration> myRegs = registrationRepository.findByUser(user);
        model.addAttribute("registrations", myRegs);
        return "student/my-events";
    }
}
