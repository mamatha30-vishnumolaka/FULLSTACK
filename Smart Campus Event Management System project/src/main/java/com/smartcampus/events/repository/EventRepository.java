package com.smartcampus.events.repository;

import com.smartcampus.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDateTime date);
    
    @Query("SELECT e FROM Event e WHERE " +
           "(:type IS NULL OR e.type = :type) AND " +
           "(:department IS NULL OR e.department = :department) AND " +
           "(:searchTerm IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY e.eventDate ASC")
    List<Event> searchEvents(@Param("type") String type, 
                             @Param("department") String department, 
                             @Param("searchTerm") String searchTerm);
}
