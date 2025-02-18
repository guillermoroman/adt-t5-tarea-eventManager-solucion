package adt.t5.eventManager.services;


import adt.t5.eventManager.entities.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueService {

    List<Venue> findAll();
    Optional<Venue> findById(Long id);
    Venue save(Venue venue);
    void deleteById(Long id);
    void deleteAll();
    List<Venue> findAllByName(String name);

}