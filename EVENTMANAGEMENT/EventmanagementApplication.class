package com.campus.eventmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String department;
    private String type;
    private String date;
    private String venue;
    private int availableSeats;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}