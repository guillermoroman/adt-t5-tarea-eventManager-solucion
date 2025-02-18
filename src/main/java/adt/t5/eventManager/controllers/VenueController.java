package adt.t5.eventManager.controllers;

import adt.t5.eventManager.entities.Venue;
import adt.t5.eventManager.services.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    // GET: Listar todos los venues
    @GetMapping
    public List<Venue> getAllVenues() {
        return venueService.findAll();
    }

    // GET: Venue por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getVenueById(@PathVariable Long id) {
        return venueService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear nuevo Venue
    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        // Aquí podríamos manejar la subida de imagen si viniera en el body
        // o en MultipartFile con otro método, etc.
        Venue createdVenue = venueService.save(venue);
        return new ResponseEntity<>(createdVenue, HttpStatus.CREATED);
    }

    // PUT: Editar un Venue existente
    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(@PathVariable Long id, @RequestBody Venue venueDetails) {
        return venueService.findById(id)
                .map(venue -> {
                    venue.setName(venueDetails.getName());
                    venue.setLocation(venueDetails.getLocation());
                    venue.setImage(venueDetails.getImage());
                    // Si VenueDetails incluye events, habría que manejarlo con cuidado
                    // Normalmente no se gestiona la lista de events directamente aquí
                    Venue updated = venueService.save(venue);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Eliminar un Venue por ID (los Events asociados se eliminarán en cascada)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE: Eliminar todos los venues
    @DeleteMapping
    public ResponseEntity<Void> deleteAllVenues() {
        venueService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    // GET: Buscar Venues por nombre
    @GetMapping("/byName")
    public List<Venue> getVenuesByName(@RequestParam String name) {
        return venueService.findAllByName(name);
    }

    @PostMapping(value = "/{id}/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Venue> uploadImageForVenue(@PathVariable Long id, @RequestParam("file") MultipartFile file){

        // Comprobar que existe el Venue al que se quiere asignar la imagen.
        Optional<Venue> optionalVenue = venueService.findById(id);
        if(optionalVenue.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Venue venue = optionalVenue.get();

        // Directorio local donde guardaremos la imagen (en paralelo a tu ImageController)
        final String UPLOAD_DIR = "uploads/";

        try {
            // 1. Asegurar que existe el directorio de subida
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // 2. Generar un nombre de archivo (puedes usar el original o algo único)
            // Aquí, añadimos un timestamp para evitar colisiones
            String originalFilename = file.getOriginalFilename();
            String fileName = System.currentTimeMillis() + "-" + (originalFilename != null ? originalFilename : "image.jpg");

            // 3. Crear la ruta absoluta donde se guardará el fichero
            Path targetLocation = uploadPath.resolve(fileName);

            // 4. Copiar el contenido del MultipartFile al directorio de destino
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 5. Actualizar el campo 'image' del Venue con el nombre del fichero
            venue.setImage(fileName);

            // 6. Guardar los cambios
            Venue updated = venueService.save(venue);

            // 7. Devolver el Venue actualizado
            return ResponseEntity.ok(updated);

        } catch (IOException e) {
            // Si hay un error al escribir el fichero
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }

    }


}