package adt.t5.eventManager.repositories;

import adt.t5.eventManager.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    // Búsqueda por nombre
    List<Venue> findAllByName(String name);

}