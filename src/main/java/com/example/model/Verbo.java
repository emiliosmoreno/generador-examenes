package com.example.model;

import jakarta.persistence.*;

@Entity
public class Verbo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String infinitivo;
    private String pasadoSimple;
    private String participioPasado;
    private String significado;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getInfinitivo() { return infinitivo; }
    public void setInfinitivo(String infinitivo) { this.infinitivo = infinitivo; }
    public String getPasadoSimple() { return pasadoSimple; }
    public void setPasadoSimple(String pasadoSimple) { this.pasadoSimple = pasadoSimple; }
    public String getParticipioPasado() { return participioPasado; }
    public void setParticipioPasado(String participioPasado) { this.participioPasado = participioPasado; }
    public String getSignificado() { return significado; }
    public void setSignificado(String significado) { this.significado = significado; }
}
