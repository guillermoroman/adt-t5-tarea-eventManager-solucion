package adt.t5.eventManager.repositories;


import adt.t5.eventManager.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Buscar por fecha
    List<Event> findByDate(LocalDate date);

    // Búsqueda por nombre del Venue (a través del campo 'venue.name')
    List<Event> findByVenue_Name(String venueName);

}