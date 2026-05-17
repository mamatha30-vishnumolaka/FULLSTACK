package com.smartcampus.events.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

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

import com.google.zxing.WriterException;
import com.smartcampus.events.model.Event;
import com.smartcampus.events.model.Payment;
import com.smartcampus.events.model.Registration;
import com.smartcampus.events.model.User;
import com.smartcampus.events.repository.EventRepository;
import com.smartcampus.events.repository.PaymentRepository;
import com.smartcampus.events.repository.RegistrationRepository;
import com.smartcampus.events.repository.UserRepository;
import com.smartcampus.events.service.QRCodeService;

@Controller
@RequestMapping("/student/payments")
public class PaymentController {

    private static final BigDecimal FIXED_EVENT_FEE = BigDecimal.valueOf(200.00);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QRCodeService qrCodeService;

    @GetMapping("/pay/event/{eventId}")
    public String showEventPaymentForm(@PathVariable Long eventId, Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

        if (registrationRepository.existsByUserAndEvent(user, event)) {
            return "redirect:/student/payments";
        }

        int currentRegs = (int) registrationRepository.countByEvent(event);
        if (currentRegs >= event.getMaxCapacity()) {
            return "redirect:/student/events";
        }

        BigDecimal amount = FIXED_EVENT_FEE;
        String transactionId = "TXN-" + System.currentTimeMillis();

        preparePaymentForm(model, amount, transactionId, event.getTitle());

        model.addAttribute("event", event);
        model.addAttribute("amount", amount);
        model.addAttribute("transactionId", transactionId);
        model.addAttribute("user", user);
        model.addAttribute("paymentAction", "/student/payments/pay/event/" + eventId);
        return "student/payment-form";
    }

    private void preparePaymentForm(Model model, BigDecimal amount, String transactionId, String eventTitle) {
        String errorMessage = null;

        try {
            model.addAttribute("creditCardQRCode", qrCodeService.generatePaymentQRCode("Credit Card", amount, transactionId, eventTitle));
            model.addAttribute("cardQRCode", qrCodeService.generatePaymentQRCode("Debit Card", amount, transactionId, eventTitle));
            model.addAttribute("creditCardInstructions", qrCodeService.getPaymentInstructions("Credit Card"));
            model.addAttribute("cardInstructions", qrCodeService.getPaymentInstructions("Debit Card"));
        } catch (WriterException | IOException e) {
            if (errorMessage == null) {
                errorMessage = "Payment QR code generation failed. Please try again later.";
            }
        }

        model.addAttribute("razorpayAvailable", false);
        if (errorMessage != null) {
            model.addAttribute("qrCodeError", errorMessage);
        }
    }

    @PostMapping("/pay/event/{eventId}")
    public String processEventPayment(@PathVariable Long eventId,
                                      @RequestParam(required = false, defaultValue = "Razorpay") String paymentMethod,
                                      @RequestParam BigDecimal amount,
                                      @RequestParam(required = false) String razorpayPaymentId,
                                      @RequestParam(required = false) String razorpayOrderId,
                                      @RequestParam(required = false) String razorpaySignature,
                                      Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Invalid event ID"));

        if (registrationRepository.existsByUserAndEvent(user, event)) {
            return "redirect:/student/events/my-events?error=Already%20registered";
        }

        int currentRegs = (int) registrationRepository.countByEvent(event);
        if (currentRegs >= event.getMaxCapacity()) {
            return "redirect:/student/events?error=Event%20is%20full";
        }

        Registration registration = new Registration(user, event);
        registrationRepository.save(registration);

        Payment payment = new Payment(registration, amount, paymentMethod);
        if (razorpayPaymentId != null && !razorpayPaymentId.isEmpty()) {
            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setRazorpaySignature(razorpaySignature);
        }
        payment.setStatus("COMPLETED");
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        paymentRepository.save(payment);

        String eventTitle = URLEncoder.encode(registration.getEvent().getTitle(), StandardCharsets.UTF_8);
        return "redirect:/student/payments/success?eventTitle=" + eventTitle;
    }

    @GetMapping
    public String viewPayments(Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        List<Registration> registrations = registrationRepository.findByUser(user);
        model.addAttribute("registrations", registrations);
        return "student/payments";
    }

    @GetMapping("/pay/{registrationId}")
    public String showPaymentForm(@PathVariable Long registrationId, Model model, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Optional<Registration> registrationOpt = registrationRepository.findById(registrationId);

        if (registrationOpt.isEmpty() || !registrationOpt.get().getUser().equals(user)) {
            return "redirect:/student/payments";
        }

        Registration registration = registrationOpt.get();
        BigDecimal eventPrice = registration.getEvent().getPrice();

        // Check if payment already exists
        List<Payment> existingPayments = paymentRepository.findByRegistration(registration);
        if (!existingPayments.isEmpty()) {
            return "redirect:/student/payments";
        }

        String transactionId = "TXN-" + System.currentTimeMillis();
        preparePaymentForm(model, eventPrice, transactionId, registration.getEvent().getTitle());

        model.addAttribute("registration", registration);
        model.addAttribute("event", registration.getEvent());
        model.addAttribute("user", user);
        model.addAttribute("paymentAction", "/student/payments/pay/" + registrationId);
        model.addAttribute("amount", eventPrice);
        model.addAttribute("transactionId", transactionId);
        return "student/payment-form";
    }

    @PostMapping("/pay/{registrationId}")
    public String processPayment(@PathVariable Long registrationId,
                                @RequestParam String paymentMethod,
                                @RequestParam BigDecimal amount,
                                @RequestParam(required = false) String razorpayPaymentId,
                                @RequestParam(required = false) String razorpayOrderId,
                                @RequestParam(required = false) String razorpaySignature,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        Optional<Registration> registrationOpt = registrationRepository.findById(registrationId);

        if (registrationOpt.isEmpty() || !registrationOpt.get().getUser().equals(user)) {
            redirectAttributes.addFlashAttribute("error", "Invalid registration.");
            return "redirect:/student/payments";
        }

        Registration registration = registrationOpt.get();

        // Check if payment already exists
        List<Payment> existingPayments = paymentRepository.findByRegistration(registration);
        if (!existingPayments.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Payment already processed.");
            return "redirect:/student/payments";
        }

        // For demo purposes, simulate payment processing
        Payment payment = new Payment(registration, amount, paymentMethod);
        if (razorpayPaymentId != null && !razorpayPaymentId.isEmpty()) {
            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setRazorpaySignature(razorpaySignature);
        }
        payment.setStatus("COMPLETED"); // In real app, this would be pending until confirmed
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        paymentRepository.save(payment);

        String eventTitle = URLEncoder.encode(registration.getEvent().getTitle(), StandardCharsets.UTF_8);
        return "redirect:/student/payments/success?eventTitle=" + eventTitle;
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam(required = false) String eventTitle, Model model) {
        model.addAttribute("eventTitle", eventTitle);
        return "student/payment-success";
    }
}