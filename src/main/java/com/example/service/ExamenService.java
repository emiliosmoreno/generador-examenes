package com.example.service;

import com.example.model.Pregunta;
import com.example.model.Examen;
import com.example.model.Resultado;
import com.example.repository.PreguntaRepository;
import com.example.repository.ExamenRepository;
import com.example.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class ExamenService {
    @Autowired
    private PreguntaRepository preguntaRepository;
    @Autowired
    private ExamenRepository examenRepository;
    @Autowired
    private ResultadoRepository resultadoRepository;

    public List<Pregunta> obtenerPreguntasAleatorias(int cantidad) {
        List<Pregunta> todas = preguntaRepository.findAll();
        Collections.shuffle(todas);
        return todas.subList(0, Math.min(cantidad, todas.size()));
    }

    public Examen crearExamen(String tipo) {
        Examen examen = new Examen();
        examen.setFechaCreacion(java.time.LocalDateTime.now());
        examen.setTipo(tipo);
        return examenRepository.save(examen);
    }

    public Resultado guardarResultado(Resultado resultado) {
        return resultadoRepository.save(resultado);
    }

    public List<Resultado> obtenerResultados() {
        return resultadoRepository.findAll();
    }

    public void eliminarResultados() {
        resultadoRepository.deleteAll();
        examenRepository.deleteAll();
    }

    public Examen obtenerExamenPorId(Long id) {
        return examenRepository.findById(id).orElse(null);
    }

    public Pregunta obtenerPreguntaPorId(Long id) {
        return preguntaRepository.findById(id).orElse(null);
    }

    public static class ResumenExamen {
        public Long examenId;
        public java.time.LocalDateTime fechaCreacion;
        public int totalPreguntas;
        public int aciertos;
        public ResumenExamen(Long examenId, java.time.LocalDateTime fechaCreacion, int totalPreguntas, int aciertos) {
            this.examenId = examenId;
            this.fechaCreacion = fechaCreacion;
            this.totalPreguntas = totalPreguntas;
            this.aciertos = aciertos;
        }
        public boolean isAprobado() {
            // Aprobado si aciertos >= 5 (exámenes siempre de 10 preguntas)
            return aciertos >= 5;
        }
    }

    public List<ResumenExamen> obtenerResumenesExamen() {
        List<Examen> examenes = examenRepository.findAll();
        List<Resultado> resultados = resultadoRepository.findAll();
        Map<Long, List<Resultado>> resultadosPorExamen = new HashMap<>();
        for (Resultado r : resultados) {
            resultadosPorExamen.computeIfAbsent(r.getExamenId(), k -> new ArrayList<>()).add(r);
        }
        List<ResumenExamen> resumenes = new ArrayList<>();
        for (Examen examen : examenes) {
            List<Resultado> res = resultadosPorExamen.getOrDefault(examen.getId(), new ArrayList<>());
            int total = res.size();
            int aciertos = (int) res.stream().filter(Resultado::isCorrecta).count();
            resumenes.add(new ResumenExamen(examen.getId(), examen.getFechaCreacion(), total, aciertos));
        }
        return resumenes;
    }
}
