package adt.t5.eventManager.services;


import adt.t5.eventManager.entities.Event;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> findAll();
    Optional<Event> findById(Long id);
    Event save(Event event);
    void deleteById(Long id);
    void deleteAll();
    List<Event> findByVenueName(String venueName);
    List<Event> findByDate(LocalDate date);
}