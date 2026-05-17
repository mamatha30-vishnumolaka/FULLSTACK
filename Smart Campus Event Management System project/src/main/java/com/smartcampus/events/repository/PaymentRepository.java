package com.smartcampus.events.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcampus.events.model.Payment;
import com.smartcampus.events.model.Registration;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByRegistration(Registration registration);
    List<Payment> findByStatus(String status);
}