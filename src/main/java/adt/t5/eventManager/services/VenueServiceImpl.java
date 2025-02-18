package adt.t5.eventManager.services;


import adt.t5.eventManager.entities.Venue;
import adt.t5.eventManager.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id);
    }

    @Override
    public Venue save(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    public void deleteAll() {
        venueRepository.deleteAll();
    }

    @Override
    public List<Venue> findAllByName(String name) {
        return venueRepository.findAllByName(name);
    }

    private static final String UPLOAD_DIR = "uploads";

    @Override
    public void deleteById(Long id) {
        // Primero obtenemos el Venue, para conocer el nombre de la imagen
        Optional<Venue> optionalVenue = venueRepository.findById(id);
        if (optionalVenue.isPresent()) {
            Venue venue = optionalVenue.get();

            // Si hay una imagen asociada, intentamos borrarla del directorio
            String imageName = venue.getImage();
            if (imageName != null && !imageName.isEmpty()) {
                try {
                    Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                    Path imagePath = uploadPath.resolve(imageName);

                    // Borra el archivo si existe
                    Files.deleteIfExists(imagePath);

                } catch (IOException e) {
                    // Manejo de excepción (log, rethrow, etc.)
                    e.printStackTrace();
                }
            }

            // Ahora sí, eliminamos el Venue en la base de datos
            venueRepository.deleteById(id);
        }
    }
}