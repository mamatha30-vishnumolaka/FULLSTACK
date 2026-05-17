package com.smartcampus.events.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartcampus.events.service.ChatbotService;

@Controller
@RequestMapping("/innovation")
public class InnovationController {

    @Autowired
    private ChatbotService chatbotService;

    @GetMapping
    public String innovationHome(Model model) {
        return "innovation";
    }

    @PostMapping(value = "/chatbot/message")
    @ResponseBody
    public Map<String, String> chatbotMessage(@RequestBody Map<String, String> payload) {
        String message = payload.getOrDefault("message", "");
        String response = chatbotService.respond(message);
        Map<String, String> result = new HashMap<>();
        result.put("reply", response);
        return result;
    }
}
