package com.smartcampus.events.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcampus.events.model.Event;
import com.smartcampus.events.model.Payment;
import com.smartcampus.events.repository.EventRepository;
import com.smartcampus.events.repository.PaymentRepository;
import com.smartcampus.events.repository.RegistrationRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/events")
public class AdminEventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping
    public String listEvents(@RequestParam(required = false) String type, 
                             @RequestParam(required = false) String department,
                             @RequestParam(required = false) String search,
                             Model model) {
        List<Event> events;
        if(type != null || department != null || search != null) {
            String qType = (type != null && type.isEmpty()) ? null : type;
            String qDept = (department != null && department.isEmpty()) ? null : department;
            String qSearch = (search != null && search.isEmpty()) ? null : search;
            events = eventRepository.searchEvents(qType, qDept, qSearch);
        } else {
            events = eventRepository.findAll();
        }
        
        // Populate registration counts
        events.forEach(e -> e.setCurrentRegistrations((int)registrationRepository.countByEvent(e)));
        
        model.addAttribute("events", events);
        model.addAttribute("type", type);
        model.addAttribute("department", department);
        model.addAttribute("search", search);
        return "admin/events-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/event-form";
    }

    @PostMapping("/create")
    public String createEvent(@Valid @ModelAttribute("event") Event event, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/event-form";
        }
        eventRepository.save(event);
        return "redirect:/admin/events?success";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event ID:" + id));
        model.addAttribute("event", event);
        return "admin/event-form";
    }

    @PostMapping("/edit/{id}")
    public String updateEvent(@PathVariable Long id, @Valid @ModelAttribute("event") Event event, BindingResult result) {
        if (result.hasErrors()) {
            event.setId(id);
            return "admin/event-form";
        }
        eventRepository.save(event);
        return "redirect:/admin/events?updated";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid event ID:" + id));
        eventRepository.delete(event);
        return "redirect:/admin/events?deleted";
    }

    @GetMapping("/stats")
    public String showStats(Model model) {
        List<Event> events = eventRepository.findAll();
        long totalEvents = events.size();
        long totalRegistrations = registrationRepository.count();
        long totalPayments = paymentRepository.count();
        
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("totalRegistrations", totalRegistrations);
        model.addAttribute("totalPayments", totalPayments);
        model.addAttribute("events", events);
        return "admin/stats";
    }

    @GetMapping("/payments")
    public String viewPayments(Model model) {
        List<Event> events = eventRepository.findAll();
        List<Payment> payments = paymentRepository.findAll();
        long completedPayments = paymentRepository.findByStatus("COMPLETED").size();
        long pendingPayments = paymentRepository.findByStatus("PENDING").size();

        model.addAttribute("events", events);
        model.addAttribute("payments", payments);
        model.addAttribute("completedPayments", completedPayments);
        model.addAttribute("pendingPayments", pendingPayments);
        model.addAttribute("totalRegistrations", registrationRepository.count());
        return "admin/payments";
    }
}
