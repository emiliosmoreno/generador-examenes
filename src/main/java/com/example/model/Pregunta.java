package com.example.model;

import jakarta.persistence.*;

@Entity
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String frase1;
    private String frase2;
    @Column(name = "tiempo_verbal1_id")
    private int tiempoVerbal1Id;
    @Column(name = "tiempo_verbal2_id")
    private int tiempoVerbal2Id;
    private boolean afirmativa;
    private int verboId;
    private String explicacionCorrecta;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFrase1() { return frase1; }
    public void setFrase1(String frase1) { this.frase1 = frase1; }
    public String getFrase2() { return frase2; }
    public void setFrase2(String frase2) { this.frase2 = frase2; }
    public int getTiempoVerbal1Id() { return tiempoVerbal1Id; }
    public void setTiempoVerbal1Id(int tiempoVerbal1Id) { this.tiempoVerbal1Id = tiempoVerbal1Id; }
    public int getTiempoVerbal2Id() { return tiempoVerbal2Id; }
    public void setTiempoVerbal2Id(int tiempoVerbal2Id) { this.tiempoVerbal2Id = tiempoVerbal2Id; }
    public boolean isAfirmativa() { return afirmativa; }
    public void setAfirmativa(boolean afirmativa) { this.afirmativa = afirmativa; }
    public int getVerboId() { return verboId; }
    public void setVerboId(int verboId) { this.verboId = verboId; }
    public String getExplicacionCorrecta() { return explicacionCorrecta; }
    public void setExplicacionCorrecta(String explicacionCorrecta) { this.explicacionCorrecta = explicacionCorrecta; }
}
