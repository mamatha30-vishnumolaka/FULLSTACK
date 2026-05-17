package com.smartcampus.events.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.smartcampus.events.model.Event;
import com.smartcampus.events.model.User;
import com.smartcampus.events.repository.EventRepository;
import com.smartcampus.events.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("Super Admin");
            admin.setEmail("admin@smartcampus.edu");
            admin.setRole("ROLE_ADMIN");
            admin.setDepartment("IT");
            userRepository.save(admin);

            User student = new User();
            student.setUsername("student1");
            student.setPassword(passwordEncoder.encode("password"));
            student.setName("John Doe");
            student.setEmail("johndoe@student.edu");
            student.setRole("ROLE_STUDENT");
            student.setDepartment("Computer Science");
            student.setAcademicYear("3rd Year");
            student.setBranch("CSE");
            userRepository.save(student);

            Event event1 = new Event();
            event1.setTitle("Spring Boot Workshop");
            event1.setDescription("Learn the basics of Spring Boot and microservices.");
            event1.setDepartment("Computer Science");
            event1.setType("Workshop");
            event1.setEventDate(LocalDateTime.now().plusDays(5));
            event1.setMaxCapacity(50);
            event1.setLocation("Lab 4A");
            event1.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event1);

            Event event2 = new Event();
            event2.setPrice(BigDecimal.valueOf(200.00));
            event2.setTitle("AI & Machine Learning Seminar");
            event2.setDescription("Deep dive into modern AI applications.");
            event2.setDepartment("Data Science");
            event2.setType("Seminar");
            event2.setEventDate(LocalDateTime.now().plusDays(10));
            event2.setMaxCapacity(100);
            event2.setLocation("Main Auditorium");
            eventRepository.save(event2);

            Event event3 = new Event();
            event3.setTitle("Cybersecurity Fundamentals");
            event3.setDescription("Learn the basics of network security, ethical hacking, and defense techniques.");
            event3.setDepartment("Computer Science");
            event3.setType("Workshop");
            event3.setEventDate(LocalDateTime.now().plusDays(7));
            event3.setMaxCapacity(40);
            event3.setLocation("Lab 2B");
            event3.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event3);

            Event event4 = new Event();
            event4.setTitle("Business Innovation Summit");
            event4.setDescription("A gathering of industry leaders discussing the future of tech businesses.");
            event4.setDepartment("Business");
            event4.setType("Seminar");
            event4.setEventDate(LocalDateTime.now().plusDays(14));
            event4.setMaxCapacity(150);
            event4.setLocation("Conference Center");
            event4.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event4);

            Event event5 = new Event();
            event5.setTitle("Campus Cultural Festival");
            event5.setDescription("Annual cultural activities featuring music, art, and performances by students.");
            event5.setDepartment("General");
            event5.setType("Activity");
            event5.setEventDate(LocalDateTime.now().plusDays(20));
            event5.setMaxCapacity(500);
            event5.setLocation("University Quad");
            event5.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event5);

            Event event6 = new Event();
            event6.setTitle("Web Development Bootcamp");
            event6.setDescription("Intensive 2-day bootcamp covering React, Node.js, and modern CSS frameworks.");
            event6.setDepartment("Computer Science");
            event6.setType("Workshop");
            event6.setEventDate(LocalDateTime.now().plusDays(3));
            event6.setMaxCapacity(30);
            event6.setLocation("Lab 1C");
            event6.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event6);

            Event event7 = new Event();
            event7.setTitle("Entrepreneurship Pitch Networking");
            event7.setDescription("Connect with local investors and pitch your startup ideas in front of a live audience.");
            event7.setDepartment("Business");
            event7.setType("Activity");
            event7.setEventDate(LocalDateTime.now().plusDays(12));
            event7.setMaxCapacity(80);
            event7.setLocation("Student Union Ballroom");
            event7.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event7);

            Event event8 = new Event();
            event8.setTitle("Cloud Computing Architectures");
            event8.setDescription("Guest lecture from AWS engineers detailing scalable cloud backend designs.");
            event8.setDepartment("Computer Science");
            event8.setType("Seminar");
            event8.setEventDate(LocalDateTime.now().plusDays(8));
            event8.setMaxCapacity(120);
            event8.setLocation("Virtual (Zoom)");
            event8.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event8);

            Event event9 = new Event();
            event9.setTitle("Robotics Competition");
            event9.setDescription("Annual engineering battlebots and automated drone racing event.");
            event9.setDepartment("Engineering");
            event9.setType("Activity");
            event9.setEventDate(LocalDateTime.now().plusDays(25));
            event9.setMaxCapacity(200);
            event9.setLocation("Engineering Courtyard");
            event9.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event9);

            Event event10 = new Event();
            event10.setTitle("Data Visualization Workshop");
            event10.setDescription("Hands-on training using Tableau and Python for beautiful data reporting.");
            event10.setDepartment("Data Science");
            event10.setType("Workshop");
            event10.setEventDate(LocalDateTime.now().plusDays(6));
            event10.setMaxCapacity(45);
            event10.setLocation("Lab 3A");
            event10.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event10);
            Event event11 = new Event();
            event11.setTitle("URGENT ALARM DEMO EVENT");
            event11.setDescription("This event is physically scheduled to start right now. Register for it to instantly trigger the audio alarm test!");
            event11.setDepartment("General");
            event11.setType("Activity");
            event11.setEventDate(LocalDateTime.now().plusMinutes(3));
            event11.setMaxCapacity(999);
            event11.setLocation("Alarms Testing Center");
            event11.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event11);

            Event event12 = new Event();
            event12.setTitle("Blockchain and Web3 Seminar");
            event12.setDescription("Explore the world of decentralized applications and smart contracts.");
            event12.setDepartment("Computer Science");
            event12.setType("Seminar");
            event12.setEventDate(LocalDateTime.now().plusDays(15));
            event12.setMaxCapacity(80);
            event12.setLocation("Virtual (Zoom)");
            event12.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event12);

            Event event13 = new Event();
            event13.setTitle("Hackathon 2026");
            event13.setDescription("A 48-hour coding marathon to solve real-world campus problems.");
            event13.setDepartment("Engineering");
            event13.setType("Activity");
            event13.setEventDate(LocalDateTime.now().plusDays(30));
            event13.setMaxCapacity(300);
            event13.setLocation("Main Library");
            event13.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event13);

            Event event14 = new Event();
            event14.setTitle("Financial Planning for Students");
            event14.setDescription("Expert advice on managing student loans, investing, and budgeting.");
            event14.setDepartment("Business");
            event14.setType("Seminar");
            event14.setEventDate(LocalDateTime.now().plusDays(9));
            event14.setMaxCapacity(150);
            event14.setLocation("Auditorium B");
            event14.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event14);

            Event event15 = new Event();
            event15.setTitle("Digital Marketing Masterclass");
            event15.setDescription("Learn SEO, social media strategies, and content creation for modern businesses.");
            event15.setDepartment("Business");
            event15.setType("Workshop");
            event15.setEventDate(LocalDateTime.now().plusDays(11));
            event15.setMaxCapacity(60);
            event15.setLocation("Lab 5");
            event15.setPrice(BigDecimal.valueOf(200.00));
            eventRepository.save(event15);
        }
    }
}
