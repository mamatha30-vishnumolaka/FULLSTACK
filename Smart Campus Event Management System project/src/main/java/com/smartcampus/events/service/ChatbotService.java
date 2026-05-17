package com.smartcampus.events.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatbotService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String respond(String message) {
        String clean = message == null ? "" : message.trim().toLowerCase();

        if (clean.contains("weather")) {
            return "Ask me about a campus event location and I can open the map details for you. For weather, open an event details page and check the weather panel there.";
        }

        if (clean.contains("event") || clean.contains("register") || clean.contains("campus")) {
            return "Smart Campus helps you search, view, and register for events. Use the event list to see upcoming campus workshops, seminars, and cultural activities.";
        }

        if (clean.contains("otp") || clean.contains("verification")) {
            return "The registration flow now includes OTP verification. Submit your details and enter the code on the next screen to complete signup.";
        }

        if (clean.contains("map") || clean.contains("location")) {
            return "Event details pages include a smart campus map built with OpenStreetMap. Open any event to see its location and live weather information.";
        }

        try {
            Map<?, ?> result = restTemplate.getForObject("https://api.adviceslip.com/advice", Map.class);
            if (result != null && result.containsKey("slip")) {
                Object slipObj = result.get("slip");
                if (slipObj instanceof Map<?, ?> slip) {
                    Object advice = slip.get("advice");
                    if (advice != null) {
                        return "Advice from the campus coach: \"" + advice.toString() + "\"";
                    }
                }
            }
        } catch (RuntimeException ignored) {
            // ignore third-party API failure and fallback below
        }

        return "I am your Smart Campus assistant. Try asking about events, maps, OTP registration, or campus navigation.";
    }
}
