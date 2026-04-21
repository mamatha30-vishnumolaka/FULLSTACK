package com.campus.eventmanagement.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.campus.eventmanagement.model.Event;
import com.campus.eventmanagement.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository repo;
    public boolean registerForEvent(Long eventId, int tickets) {

    Event event = repo.findById(eventId).orElse(null);

    if (event == null) return false;

    if (event.getAvailableSeats() < tickets) {
        return false;
    }

    event.setAvailableSeats(event.getAvailableSeats() - tickets);
    repo.save(event);

    return true;
}
    public List<Event> getAllEvents() {
        return repo.findAll();
    }

    public Event saveEvent(Event event) {
        return repo.save(event);
    }

    public void deleteEvent(Long id) {
        repo.deleteById(id);
    }
}