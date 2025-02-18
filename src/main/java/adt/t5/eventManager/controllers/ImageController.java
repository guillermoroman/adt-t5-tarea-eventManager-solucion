package adt.t5.eventManager.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final String UPLOAD_DIR = "uploads/";

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Ruta absoluta del fichero
            Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Aquí, para simplificar, establecemos el Content-Type a "image/jpeg".
                // En un caso real, podrías determinar el tipo de la imagen de forma dinámica
                // (por ejemplo, usando la extensión o inspeccionando el fichero).
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            // Si algo falla al acceder al fichero, devolvemos un 500
            return ResponseEntity.internalServerError().build();
        }
    }
}
