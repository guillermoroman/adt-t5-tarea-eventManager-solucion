package adt.t5.eventManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    // En este campo podemos guardar el nombre de la imagen o la ruta donde se almacena.
    private String image;

    // Relación OneToMany con Event.
    // Al eliminar un Venue, se eliminan en cascada todos sus Event asociados.
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("venue")  // Para evitar recursión al serializar
    private List<Event> events = new ArrayList<>();

    // Constructores
    public Venue() {
    }

    public Venue(String name, String location, String image) {
        this.name = name;
        this.location = location;
        this.image = image;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}