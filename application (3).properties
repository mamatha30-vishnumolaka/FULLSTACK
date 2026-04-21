package com.campus.eventmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.campus.eventmanagement.model.Event;
import com.campus.eventmanagement.service.EventService;
import com.campus.eventmanagement.registration.Registration;
import com.campus.eventmanagement.registration.RegistrationRepository;

@Controller
public class EventController {

    @Autowired
    private EventService service;

    @Autowired
    private RegistrationRepository regRepo;
@GetMapping("/registrations")
public String viewRegistrations(Model model) {
    model.addAttribute("regs", regRepo.findAll());
    return "registrations";
}
    // =========================
    // STUDENT: View all events
    // =========================
    @GetMapping("/")
    public String viewEvents(Model model) {
        model.addAttribute("events", service.getAllEvents());
        return "index";
    }

    // =========================
    // ADMIN: Dashboard
    // =========================
    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("events", service.getAllEvents());
        model.addAttribute("event", new Event());
        return "admin";
    }

    // =========================
    // ADMIN: Save Event
    // =========================
    @PostMapping("/save")
    public String saveEvent(@ModelAttribute Event event) {
        service.saveEvent(event);
        return "redirect:/admin";
    }

    // =========================
    // ADMIN: Delete Event
    // =========================
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return "redirect:/admin";
    }

    // =========================
    // STUDENT: Show Register Form
    // =========================
    @GetMapping("/register/{id}")
    public String showRegisterForm(@PathVariable Long id, Model model) {
        model.addAttribute("eventId", id);
        model.addAttribute("registration", new Registration());
        return "register";
    }

    // =========================
    // STUDENT: Submit Registration
    // =========================
    @PostMapping("/register")
    public String registerEvent(@ModelAttribute Registration reg, Model model) {

        boolean success = service.registerForEvent(reg.getEventId(), reg.getTickets());

        if (!success) {
            model.addAttribute("error", "Not enough seats available!");
            model.addAttribute("eventId", reg.getEventId());
            return "register";
        }

        regRepo.save(reg);
        return "success";
    }
}