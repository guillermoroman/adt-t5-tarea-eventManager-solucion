package adt.t5.eventManager.controllers;

import adt.t5.eventManager.entities.Event;
import adt.t5.eventManager.entities.Venue;
import adt.t5.eventManager.services.EventService;
import adt.t5.eventManager.services.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private VenueService venueService;

    // GET: Listar todos los eventos
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.findAll();
    }

    // GET: Event por ID
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear un nuevo Event, asegurando que el Venue existe
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        // Verificar que el venue del event existe
        if (event.getVenue() == null || event.getVenue().getId() == null) {
            // Venue no proporcionado
            return ResponseEntity.badRequest().build();
        }

        Optional<Venue> venueOpt = venueService.findById(event.getVenue().getId());
        if (venueOpt.isEmpty()) {
            // Venue no existe
            return ResponseEntity.badRequest().build();
        }

        // Asignamos la entidad Venue real al Event
        event.setVenue(venueOpt.get());

        // Guardar el event
        Event createdEvent = eventService.save(event);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    // PUT: Editar un Event existente
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Optional<Event> optionalEvent = eventService.findById(id);

        if (optionalEvent.isPresent()) {
            Event existingEvent = optionalEvent.get();
            existingEvent.setName(eventDetails.getName());
            existingEvent.setDate(eventDetails.getDate());

            // Si viene un Venue nuevo, comprobar que existe
            if (eventDetails.getVenue() != null && eventDetails.getVenue().getId() != null) {
                Optional<Venue> venueOpt = venueService.findById(eventDetails.getVenue().getId());
                if (venueOpt.isEmpty()) {
                    // El venue no existe
                    return ResponseEntity.badRequest().build();
                }
                existingEvent.setVenue(venueOpt.get());
            }

            Event updatedEvent = eventService.save(existingEvent);
            return ResponseEntity.ok(updatedEvent);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Eliminar un Event por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE: Eliminar todos los eventos
    @DeleteMapping
    public ResponseEntity<Void> deleteAllEvents() {
        eventService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    // GET: Buscar Events por nombre de Venue
    @GetMapping("/byVenueName")
    public List<Event> getEventsByVenueName(@RequestParam String venueName) {
        return eventService.findByVenueName(venueName);
    }

    // GET: Buscar Events por fecha
    @GetMapping("/byDate")
    public List<Event> getEventsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return eventService.findByDate(date);
    }
}