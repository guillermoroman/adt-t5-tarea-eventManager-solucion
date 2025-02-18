package adt.t5.eventManager.services;

import adt.t5.eventManager.entities.Event;
import adt.t5.eventManager.repositories.EventRepository;
import adt.t5.eventManager.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event save(Event event) {
        // Podríamos validar que el venue existe antes de guardar
        // (lógica opcional de negocio)
        return eventRepository.save(event);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        eventRepository.deleteAll();
    }

    @Override
    public List<Event> findByVenueName(String venueName) {
        return eventRepository.findByVenue_Name(venueName);
    }

    @Override
    public List<Event> findByDate(LocalDate date) {
        return eventRepository.findByDate(date);
    }
}