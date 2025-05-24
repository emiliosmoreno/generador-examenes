package com.example.controller;

import com.example.model.Pregunta;
import com.example.model.Examen;
import com.example.model.Resultado;
import com.example.service.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpSession;

@Controller
public class ExamenController {
    @Autowired
    private ExamenService examenService;

    @GetMapping("/")
    public String inicio() {
        return "inicio";
    }

    @GetMapping("/examen/nuevo")
    public String nuevoExamen(Model model, HttpSession session) {
        Examen examen = examenService.crearExamen();
        List<Pregunta> preguntas = examenService.obtenerPreguntasAleatorias(10);
        session.setAttribute("examenId", examen.getId());
        session.setAttribute("preguntas", preguntas);
        model.addAttribute("examen", examen);
        model.addAttribute("preguntas", preguntas);
        model.addAttribute("indice", 0);
        model.addAttribute("preguntaActual", 1);
        model.addAttribute("totalPreguntas", preguntas.size());
        if (!preguntas.isEmpty()) {
            String frase2 = preguntas.get(0).getFrase2();
            String frase2Dialogo = frase2.replace("___", "<b>_______</b>");
            model.addAttribute("frase2Dialogo", frase2Dialogo);
        }
        return "examen";
    }

    @PostMapping("/examen/responder")
    public String responderPregunta(@RequestParam(name = "examenId") Long examenId,
                                    @RequestParam(name = "preguntaId") Long preguntaId,
                                    @RequestParam(name = "respuestaUsuario") String respuestaUsuario,
                                    @RequestParam(name = "indice") int indice,
                                    Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Pregunta> preguntas = (List<Pregunta>) session.getAttribute("preguntas");
        if (preguntas == null) {
            System.out.println("Preguntas no encontradas en la sesión, redirigiendo a nuevo examen.");
            return "redirect:/examen/nuevo";
        }
        Pregunta pregunta = preguntas.get(indice);
        Resultado resultado = new Resultado();
        resultado.setExamenId(examenId);
        resultado.setPreguntaId(preguntaId);
        resultado.setRespuestaUsuario(respuestaUsuario);
        // Extraer la respuesta correcta desde la explicación (HTML)
        String explicacion = pregunta.getExplicacionCorrecta();
        String respuestaCorrecta = null;
        if (explicacion != null && explicacion.contains("<b>Respuesta correcta:</b>")) {
            String[] partes = explicacion.split("<b>Respuesta correcta:</b>");
            if (partes.length > 1) {
                String resto = partes[1].trim();
                int fin = resto.indexOf(".");
                int br = resto.indexOf("<");
                if (fin == -1 || (br != -1 && br < fin)) fin = br;
                if (fin == -1) fin = resto.length();
                respuestaCorrecta = resto.substring(0, fin).replace(":", "").replace("<br>", "").trim();
            }
        }
        boolean correcta = false;
        if (respuestaCorrecta != null) {
            correcta = respuestaUsuario.trim().equalsIgnoreCase(respuestaCorrecta.trim());
        }
        resultado.setCorrecta(correcta);
        resultado.setExplicacion(pregunta.getExplicacionCorrecta());
        resultado.setExplicacionCorrecta(pregunta.getExplicacionCorrecta());
        examenService.guardarResultado(resultado);
        model.addAttribute("explicacion", resultado.getExplicacionCorrecta());
        model.addAttribute("correcta", resultado.isCorrecta());
        model.addAttribute("respuestaCorrecta", respuestaCorrecta);
        model.addAttribute("respuestaUsuario", respuestaUsuario);
        model.addAttribute("frase1", pregunta.getFrase1());
        model.addAttribute("frase2", pregunta.getFrase2());
        model.addAttribute("indice", indice);
        model.addAttribute("preguntaActual", indice + 1);
        model.addAttribute("totalPreguntas", preguntas.size());
        model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
        // Si es la última pregunta, mostrar resumen
        if (indice + 1 >= preguntas.size()) {
            List<Resultado> resultados = examenService.obtenerResultados().stream()
                .filter(r -> r.getExamenId().equals(examenId))
                .collect(Collectors.toList());
            long correctas = resultados.stream().filter(Resultado::isCorrecta).count();
            model.addAttribute("aciertos", correctas);
            model.addAttribute("total", preguntas.size());
            model.addAttribute("resultados", resultados);
            model.addAttribute("preguntasExamen", preguntas);
            session.removeAttribute("preguntas");
            return "resumen_examen";
        }
        return "explicacion";
    }

    @GetMapping("/examen/pasar")
    public String pasarPregunta(@RequestParam(name = "examenId") Long examenId,
                                @RequestParam(name = "indice") int indice,
                                Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Pregunta> preguntas = (List<Pregunta>) session.getAttribute("preguntas");
        if (preguntas == null) {
            return "redirect:/examen/nuevo";
        }
        int nuevoIndice = indice + 1;
        if (nuevoIndice >= preguntas.size()) {
            // Si ya no hay más preguntas, mostrar resumen
            List<Resultado> resultados = examenService.obtenerResultados().stream()
                .filter(r -> r.getExamenId().equals(examenId))
                .collect(Collectors.toList());
            long correctas = resultados.stream().filter(Resultado::isCorrecta).count();
            model.addAttribute("aciertos", correctas);
            model.addAttribute("total", preguntas.size());
            model.addAttribute("resultados", resultados);
            model.addAttribute("preguntasExamen", preguntas);
            session.removeAttribute("preguntas");
            return "resumen_examen";
        }
        model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
        model.addAttribute("preguntas", preguntas);
        model.addAttribute("indice", nuevoIndice);
        model.addAttribute("preguntaActual", nuevoIndice + 1);
        model.addAttribute("totalPreguntas", preguntas.size());
        String frase2 = preguntas.get(nuevoIndice).getFrase2();
        String frase2Dialogo = frase2.replace("___", "<b>_______</b>");
        model.addAttribute("frase2Dialogo", frase2Dialogo);
        return "examen";
    }

    @GetMapping("/resultados")
    public String verResultados(Model model) {
        model.addAttribute("resultados", examenService.obtenerResultados());
        model.addAttribute("resumenesExamen", examenService.obtenerResumenesExamen());
        return "resultados";
    }

    @PostMapping("/resultados/eliminar")
    public String eliminarResultados() {
        examenService.eliminarResultados();
        return "redirect:/resultados";
    }

    @PostMapping("/examen/finalizar")
    public String finalizarExamen(@RequestParam(name = "examenId") Long examenId, Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Pregunta> preguntas = (List<Pregunta>) session.getAttribute("preguntas");
        if (preguntas == null) {
            preguntas = examenService.obtenerPreguntasAleatorias(10);
        }
        List<Resultado> resultados = examenService.obtenerResultados().stream()
            .filter(r -> r.getExamenId().equals(examenId))
            .collect(Collectors.toList());
        long correctas = resultados.stream().filter(Resultado::isCorrecta).count();
        model.addAttribute("aciertos", correctas);
        model.addAttribute("total", preguntas.size());
        model.addAttribute("resultados", resultados);
        model.addAttribute("preguntasExamen", preguntas);
        session.removeAttribute("preguntas");
        return "resumen_examen";
    }
}
