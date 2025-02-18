package adt.t5.eventManager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Para la fecha se puede usar LocalDate, LocalDateTime o Date según convenga
    private LocalDate date;

    // Relación ManyToOne: cada Event pertenece a un Venue.
    @ManyToOne
    @JoinColumn(name = "venue_id")
    @JsonIgnoreProperties("events") // Evita recursión al serializar
    private Venue venue;

    // Constructores
    public Event() {
    }

    public Event(String name, LocalDate date, Venue venue) {
        this.name = name;
        this.date = date;
        this.venue = venue;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}