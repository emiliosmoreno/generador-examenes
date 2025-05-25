package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Examen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaCreacion;
    private String tipo; // FRASES, VERBO3, VERBO2

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // getters y setters
    // ...
}
