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
import java.util.Random;

@Controller
public class ExamenVerboController {
    @Autowired
    private VerboService verboService;
    @Autowired
    private ExamenService examenService;

    @GetMapping("/examen-verbo/nuevo")
    public String nuevoExamenVerbo(Model model, HttpSession session) {
        Examen examen = examenService.crearExamen("VERBO3");
        List<Verbo> verbos = verboService.obtenerTodos();
        Collections.shuffle(verbos);
        if (verbos.size() > 10) {
            verbos = verbos.subList(0, 10);
        }
        session.setAttribute("examenVerboId", examen.getId());
        session.setAttribute("verbos", verbos);
        model.addAttribute("examen", examen);
        model.addAttribute("verbos", verbos);
        model.addAttribute("indice", 0);
        model.addAttribute("preguntaActual", 1);
        model.addAttribute("totalPreguntas", verbos.size());
        if (!verbos.isEmpty()) {
            Verbo verbo = verbos.get(0);
            int tipo = new java.util.Random().nextInt(3) + 1;
            String dado = obtenerDadoAleatorio(tipo);
            model.addAttribute("verbo", verbo);
            model.addAttribute("tipo", tipo);
            model.addAttribute("dado", dado);
            model.addAttribute("dadoValor", obtenerValorDado(verbo, dado));
        }
        return "examen_verbo";
    }

    private String obtenerDadoAleatorio(int tipo) {
        if (tipo == 2 || tipo == 3) {
            String[] opciones = {"infinitivo", "pasadoSimple", "participioPasado"};
            return opciones[new Random().nextInt(3)];
        }
        return null;
    }

    private String obtenerValorDado(Verbo verbo, String dado) {
        if (dado == null) return "";
        switch (dado) {
            case "infinitivo": return verbo.getInfinitivo();
            case "pasadoSimple": return verbo.getPasadoSimple();
            case "participioPasado": return verbo.getParticipioPasado();
            default: return "";
        }
    }

    @PostMapping("/examen-verbo/responder")
    public String responderVerbo(@RequestParam(name = "examenId") Long examenId,
                                 @RequestParam(name = "indice") int indice,
                                 @RequestParam(name = "tipo") int tipo,
                                 @RequestParam(name = "verboId") Integer verboId,
                                 @RequestParam(name = "infinitivo", required = false) String infinitivo,
                                 @RequestParam(name = "pasadoSimple", required = false) String pasadoSimple,
                                 @RequestParam(name = "participioPasado", required = false) String participioPasado,
                                 Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Verbo> verbos = (List<Verbo>) session.getAttribute("verbos");
        if (verbos == null) {
            return "redirect:/examen-verbo/nuevo";
        }
        Verbo verbo = verbos.get(indice);
        boolean correcta = true;
        if (tipo == 1) {
            correcta = verbo.getInfinitivo().equalsIgnoreCase(infinitivo)
                    && verbo.getPasadoSimple().equalsIgnoreCase(pasadoSimple)
                    && verbo.getParticipioPasado().equalsIgnoreCase(participioPasado);
        } else if (tipo == 2 || tipo == 3) {
            if (infinitivo != null && !verbo.getInfinitivo().equalsIgnoreCase(infinitivo))
                correcta = false;
            if (pasadoSimple != null && !verbo.getPasadoSimple().equalsIgnoreCase(pasadoSimple))
                correcta = false;
            if (participioPasado != null && !verbo.getParticipioPasado().equalsIgnoreCase(participioPasado))
                correcta = false;
        }
        // Guardar respuesta en sesión
        List<Object> respuestasVerbo = (List<Object>) session.getAttribute("respuestasVerbo");
        if (respuestasVerbo == null) respuestasVerbo = new java.util.ArrayList<>();
        java.util.Map<String, Object> respuesta = new java.util.HashMap<>();
        respuesta.put("verbo", verbo);
        respuesta.put("respuestaInfinitivo", infinitivo);
        respuesta.put("respuestaPasadoSimple", pasadoSimple);
        respuesta.put("respuestaParticipioPasado", participioPasado);
        respuesta.put("correcta", correcta);
        respuestasVerbo.add(respuesta);
        session.setAttribute("respuestasVerbo", respuestasVerbo);
        // Guardar resultado en base de datos
        Resultado resultado = new Resultado();
        resultado.setExamenId(examenId);
        resultado.setPreguntaId(null); // No hay pregunta asociada
        resultado.setRespuestaUsuario(
            "Inf: " + (infinitivo != null ? infinitivo : "") +
            ", PS: " + (pasadoSimple != null ? pasadoSimple : "") +
            ", PP: " + (participioPasado != null ? participioPasado : "")
        );
        resultado.setCorrecta(correcta);
        resultado.setExplicacion(
            "Infinitivo: " + verbo.getInfinitivo() +
            ", Pasado Simple: " + verbo.getPasadoSimple() +
            ", Participio Pasado: " + verbo.getParticipioPasado()
        );
        resultado.setExplicacionCorrecta(
            "Infinitivo: " + verbo.getInfinitivo() +
            ", Pasado Simple: " + verbo.getPasadoSimple() +
            ", Participio Pasado: " + verbo.getParticipioPasado()
        );
        examenService.guardarResultado(resultado);
        model.addAttribute("correcta", correcta);
        model.addAttribute("verbo", verbo);
        model.addAttribute("infinitivoUsuario", infinitivo);
        model.addAttribute("pasadoSimpleUsuario", pasadoSimple);
        model.addAttribute("participioPasadoUsuario", participioPasado);
        model.addAttribute("tipo", tipo);
        model.addAttribute("indice", indice);
        model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
        model.addAttribute("preguntaActual", indice + 1);
        model.addAttribute("totalPreguntas", verbos.size());
        // Mostrar pantalla de explicación con botones
        return "explicacion_verbo";
    }

    @PostMapping("/examen-verbo/finalizar")
    public String finalizarVerbo(@RequestParam(name = "examenId") Long examenId, Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Verbo> verbos = (List<Verbo>) session.getAttribute("verbos");
        List<Object> respuestasVerbo = (List<Object>) session.getAttribute("respuestasVerbo");
        if (verbos == null) {
            verbos = verboService.obtenerTodos();
        }
        model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
        model.addAttribute("totalPreguntas", verbos.size());
        model.addAttribute("respuestasVerbo", respuestasVerbo);
        session.removeAttribute("verbos");
        session.removeAttribute("respuestasVerbo");
        return "resumen_examen_verbo";
    }

    @GetMapping("/examen-verbo/pasar")
    public String pasarVerbo(@RequestParam(name = "examenId") Long examenId,
                             @RequestParam(name = "indice") int indice,
                             Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Verbo> verbos = (List<Verbo>) session.getAttribute("verbos");
        if (verbos == null) {
            return "redirect:/examen-verbo/nuevo";
        }
        int nuevoIndice = indice + 1;
        if (nuevoIndice >= verbos.size()) {
            List<Object> respuestasVerbo = (List<Object>) session.getAttribute("respuestasVerbo");
            model.addAttribute("respuestasVerbo", respuestasVerbo);
            session.removeAttribute("verbos");
            session.removeAttribute("respuestasVerbo");
            model.addAttribute("examen", examenService.obtenerExamenPorId(examenId));
            model.addAttribute("totalPreguntas", verbos.size());
            return "resumen_examen_verbo";
        }
        Verbo verbo = verbos.get(nuevoIndice);
        int tipo = new java.util.Random().nextInt(3) + 1;
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
        return "examen_verbo";
    }
}
