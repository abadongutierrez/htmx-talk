package com.jabaddon.learning.lightningtalk.htmx_talk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/fragments")
public class FragmentsController {
    private static final Logger logger = LoggerFactory.getLogger(FragmentsController.class);
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ConcurrentHashMap<String, String> results = new ConcurrentHashMap<>();

    @GetMapping("/say-hello")
    public ModelAndView sayHello(@RequestParam String name, @RequestParam(required=false) String lastName) {
        List<String> values = new ArrayList<>();
        values.add(name);
        values.add(lastName);
        String wholeName = values.stream().filter(Objects::nonNull).collect(Collectors.joining(" "));
        Map<String, String> model = new HashMap<>();
        model.put("wholeName", StringUtils.isBlank(wholeName) ? "?" : wholeName);
        return new ModelAndView("fragments/say-hello", model);
    }

    @GetMapping("/echo-name-email")
    public ModelAndView echoNameEmail(@RequestParam String name, @RequestParam(required=false) String email) {
        logger.info("name: {}, email: {}", name, email);
        Map<String, Object> model = new HashMap<>();
        model.put("name", name);
        model.put("email", email);
        model.put("isEmail", !StringUtils.isBlank(email));
        return new ModelAndView("fragments/echo-name-email", model);
    }

    @PostMapping("/sum-data")
    public ModelAndView sumData(@RequestParam int a, @RequestParam int b, @RequestParam(required = false) Integer waitTime) {
        int sum = a + b;
        Map<String, Object> model = new HashMap<>();
        model.put("a", a);
        model.put("b", b);
        model.put("result", sum);
        try {
            Thread.sleep((waitTime != null ? waitTime : 0) * 1000);
        } catch (InterruptedException e) {
           logger.error("Error", e); 
        }
        return new ModelAndView("fragments/sum-data", model);
    }

    @PostMapping("/multiplication-table")
    public ModelAndView multiplicationTable(@RequestParam int number,
        @RequestParam(required = false) Float errorProbability) {
        logger.info("number: {}, errorProbability: {}", number, errorProbability);
        Map<String, Object> model = new HashMap<>();
        model.put("number", number);
        model.put("table", IntStream.range(1, 11).mapToObj(Integer::valueOf).toList());
        model.put("errorProbability", errorProbability != null ? errorProbability : 0.0);
        return new ModelAndView("fragments/multiplication-table", model);
    }

    @PostMapping("/multiply/start")
    public ModelAndView multiplicationTable(
        @RequestParam int a,
        @RequestParam int b,
        @RequestParam(required = false) Float errorProbability) {
        String taskId = a + "x" + b;
        results.put(taskId, "Calculando...");
        executor.execute(() -> {
            try {
                int delay = ThreadLocalRandom.current().nextInt(4000, 8001);
                Thread.sleep(delay);
                if (shouldFail(errorProbability)) {
                    results.put(taskId, "Error");
                } else {
                    results.put(taskId, String.valueOf(a * b));
                }
            } catch (InterruptedException e) {
                results.put(taskId, "Error");
            }
        });
        return new ModelAndView("fragments/multiply-start", Map.of("status", "Calculando...", "taskId", taskId));
    }

    private boolean shouldFail(Float errorProbability) {
        return errorProbability != null && ThreadLocalRandom.current().nextFloat() < errorProbability;
    }

    @GetMapping("/multiply/progress")
    public ModelAndView multiplicationTableStatus(@RequestParam String taskId, HttpServletResponse response) {
        List<String> statuses = List.of(
            "Aun realizando el calculo...", 
            "Cargando un LLM...", 
            "Solicituando datos al satelite...", 
            "Preguntando a la Oracle...", 
            "Pensando en la inmortalidad del cangrejo...",
            "Desanzando un rato...",
            "Preguntando a la magica bola-8...",
            "Reiniciando la Matrx...",
            "Alineando los planetas...",
            "Contando las estrellas...",
            "Ave Maria dame punteria...",
            "Preparando cafe...",
            "Encendiendo la Computadora Cuantica...",
            "Funciona en mi computadora...",
            "Creando un cluster de Kubernetes...",
            "Preguntandole a Elion Musk...",
            "Alimentando la AI..."
        );
        // randomly select a reason from reasones
        int index = ThreadLocalRandom.current().nextInt(0, statuses.size());
        String resultOrStatus = results.getOrDefault(taskId, statuses.get(index));
        if (isANumber(resultOrStatus) || resultOrStatus.equals("Error")) {
            response.addHeader("HX-Trigger", "multiply-" + taskId + "-done");
        }
        return new ModelAndView("fragments/multiply-progress",
            Map.of("status", statuses.get(index), "taskId", taskId));
    }

    @GetMapping("/multiply/done")
    public ModelAndView multiplicationTableDone(@RequestParam String taskId) {
        String result = results.getOrDefault(taskId, "Error");
        if (result.equals("Error")) {
            String[] values = taskId.split("x");
            return new ModelAndView("fragments/multiply-error",
                Map.of("status", result, "a", values[0], "b", values[1],
                "error", "Lo sientimos, no se pudo realizar la operacion"));
        }
        return new ModelAndView("fragments/multiply-progress", Map.of("status", result));
    }

    private boolean isANumber(String value) {
        return value != null && value.matches("\\d+");
    }

    @GetMapping("/basics/1")
    public String basics1() {
        return "fragments/basics1";
    }

    @PostMapping("/basics/2")
    public ModelAndView basics2(@RequestParam String name, @RequestParam String lastName) {
        return new ModelAndView("fragments/basics2", Map.of("name", name, "lastName", lastName));
    }

    @PostMapping("/basics/3")
    public ModelAndView basics3(@RequestHeader("Authentication") String token) {
        if (!"123445".equals(token)) {
            return new ModelAndView("fragments/basics3", Map.of("error", "Not authorized"));
        }
        return new ModelAndView("fragments/basics3");
    }
}

record Person(String name, String lastName) {
}
