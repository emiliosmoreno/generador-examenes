package com.example.controller;

import com.example.model.Verbo;
import com.example.model.Examen;
import com.example.model.Resultado;
import com.example.service.VerboService;
import com.example.service.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class ExamenVerbo2TiemposController {
    @Autowired
    private VerboService verboService;
    @Autowired
    private ExamenService examenService;

    @GetMapping("/examen-verbo2/nuevo")
    public String nuevoExamenVerbo2(Model model, HttpSession session) {
        Examen examen = examenService.crearExamen("VERBO2");
        List<Verbo> verbos = verboService.obtenerTodos();
        Collections.shuffle(verbos);
        if (verbos.size() > 10) {
            verbos = verbos.subList(0, 10);
        }
        session.setAttribute("examenVerbo2Id", examen.getId());
        session.setAttribute("verbos2", verbos);
        model.addAttribute("examen", examen);
        model.addAttribute("verbos", verbos);
        model.addAttribute("indice", 0);
        model.addAttribute("preguntaActual", 1);
        model.addAttribute("totalPreguntas", verbos.size());
        if (!verbos.isEmpty()) {
            Verbo verbo = verbos.get(0);
            int tipo = new java.util.Random().nextInt(2) + 1;
            String dado = obtenerDadoAleatorio(tipo);
            model.addAttribute("verbo", verbo);
            model.addAttribute("tipo", tipo);
            model.addAttribute("dado", dado);
            model.addAttribute("dadoValor", obtenerValorDado(verbo, dado));
        }
        return "examen_verbo2.html";
    }

    private String obtenerDadoAleatorio(int tipo) {
        if (tipo == 2) {
            String[] opciones = {"infinitivo", "pasadoSimple"};
            return opciones[new java.util.Random().nextInt(2)];
        }
        return null;
    }

    private String obtenerValorDado(Verbo verbo, String dado) {
        if (dado == null) return "";
        switch (dado) {
            case "infinitivo": return verbo.getInfinitivo();
            case "pasadoSimple": return verbo.getPasadoSimple();
            default: return "";
        }
    }

    @PostMapping("/examen-verbo2/responder")
    public String responderVerbo2(@RequestParam(name = "examenId") Long examenId,
                                  @RequestParam(name = "indice") int indice,
                                  @RequestParam(name = "tipo") int tipo,
                                  @RequestParam(name = "verboId") Integer verboId,
                                  @RequestParam(name = "infinitivo", required = false) String infinitivo,
                                  @RequestParam(name = "pasadoSimple", required = false) String pasadoSimple,
                                  Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Verbo> verbos = (List<Verbo>) session.getAttribute("verbos2");
        if (verbos == null) {
            return "redirect:/examen-verbo2/nuevo";
        }
        Verbo verbo = verbos.get(indice);
        boolean correcta = true;
        if (tipo == 1) {
            correcta = verbo.getInfinitivo().equalsIgnoreCase(infinitivo)
                    && verbo.getPasadoSimple().equalsIgnoreCase(pasadoSimple);
        } else if (tipo == 2) {
            if (infinitivo != null && !verbo.getInfinitivo().equalsIgnoreCase(infinitivo))
                correcta = false;
            if (pasadoSimple != null && !verbo.getPasadoSimple().equalsIgnoreCase(pasadoSimple))
                correcta = false;
        }
        // Guardar respuesta en sesión
        List<Object> respuestasVerbo2 = (List<Object>) session.getAttribute("respuestasVerbo2");
        if (respuestasVerbo2 == null) respuestasVerbo2 = new java.util.ArrayList<>();
        java.util.Map<String, Object> respuesta = new java.util.HashMap<>();
        respuesta.put("verbo", verbo);
        respuesta.put("respuestaInfinitivo", infinitivo);
        respuesta.put("respuestaPasadoSimple", pasadoSimple);
        respuesta.put("correcta", correcta);
        respuestasVerbo2.add(respuesta);
        session.setAttribute("respuestasVerbo2", respuestasVerbo2);
        // Guardar resultado en base de datos
        Resultado resultado = new Resultado();
        resultado.setExamenId(examenId);
        resultado.setPreguntaId(null); // No hay pregunta asociada
        resultado.setRespuestaUsuario("Inf: " + (infinitivo != null ? infinitivo : "") + ", PS: " + (pasadoSimple != null ? pasadoSimple : ""));
        resultado.setCorrecta(correcta);
        resultado.setExplicacion("Infinitivo: " + verbo.getInfinitivo() + ", Pasado Simple: " + verbo.getPasadoSimple());
        resultado.setExplicacionCorrecta("Infinitivo: " + verbo.getInfinitivo() + ", Pasado Simple: " + verbo.getPasadoSimple());
        examenService.guardarResultado(resultado);
        model.addAttribute("correcta", correcta);
        model.addAttribute("verbo", verbo);
        model.addAttribute("infinitivoUsuario", infinitivo);
        model.addAttribute("pasadoSimpleUsuario", pasadoSimple);
        model.addAttribute("tipo", tipo);
        model.addAttribute("indice", indice);
        model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
        model.addAttribute("preguntaActual", indice + 1);
        model.addAttribute("totalPreguntas", verbos.size());
        return "explicacion_verbo2";
    }

    @GetMapping("/examen-verbo2/pasar")
    public String pasarVerbo2(@RequestParam(name = "examenId") Long examenId,
                              @RequestParam(name = "indice") int indice,
                              Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Verbo> verbos = (List<Verbo>) session.getAttribute("verbos2");
        if (verbos == null) {
            return "redirect:/examen-verbo2/nuevo";
        }
        int nuevoIndice = indice + 1;
        if (nuevoIndice >= verbos.size()) {
            List<Object> respuestasVerbo2 = (List<Object>) session.getAttribute("respuestasVerbo2");
            model.addAttribute("respuestasVerbo2", respuestasVerbo2);
            session.removeAttribute("verbos2");
            session.removeAttribute("respuestasVerbo2");
            model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
            model.addAttribute("totalPreguntas", verbos.size());
            return "resumen_examen_verbo2";
        }
        Verbo verbo = verbos.get(nuevoIndice);
        int tipo = new java.util.Random().nextInt(2) + 1;
        String dado = obtenerDadoAleatorio(tipo);
        model.addAttribute("verbo", verbo);
        model.addAttribute("tipo", tipo);
        model.addAttribute("dado", dado);
        model.addAttribute("dadoValor", obtenerValorDado(verbo, dado));
        model.addAttribute("indice", nuevoIndice);
        model.addAttribute("preguntaActual", nuevoIndice + 1);
        model.addAttribute("totalPreguntas", verbos.size());
        model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
        model.addAttribute("verbos", verbos);
        return "examen_verbo2";
    }

    @PostMapping("/examen-verbo2/finalizar")
    public String finalizarVerbo2(@RequestParam(name = "examenId") Long examenId, Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Verbo> verbos = (List<Verbo>) session.getAttribute("verbos2");
        List<Object> respuestasVerbo2 = (List<Object>) session.getAttribute("respuestasVerbo2");
        if (verbos == null) {
            verbos = verboService.obtenerTodos();
        }
        model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
        model.addAttribute("totalPreguntas", verbos.size());
        model.addAttribute("respuestasVerbo2", respuestasVerbo2);
        session.removeAttribute("verbos2");
        session.removeAttribute("respuestasVerbo2");
        return "resumen_examen_verbo2";
    }
}
