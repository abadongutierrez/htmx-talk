package com.jabaddon.learning.lightningtalk.htmx_talk;

import java.net.URL;
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
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private static final List<String> STATUS_MESSAGES = List.of(
            "Procesando...",
            "Cargando un LLM...",
            "Solicitando datos al satelite...",
            "Preguntando a la Oracle...",
            "Pensando en la inmortalidad del cangrejo...",
            "Desansando un rato...",
            "Preguntando a la magica bola-8...",
            "Reiniciando la Matrx...",
            "Esperando la alineacion los planetas...",
            "Contando las estrellas...",
            "Ave Maria dame punteria...",
            "Preparando cafe...",
            "Encendiendo la Computadora Cuantica...",
            "Ya casi, ya casi...",
            "Creando un cluster de Kubernetes...",
            "Preguntandole a Elon Musk...",
            "Alimentando la AI...",
            "Ahorita sale...",
            "Me repites la pregunta?...",
            "Deployando 3 Microservicios...",
            "Oops, Windows se actualizo...",
            "Parece que necesitamos mas RAM...",
            "Preguntando a StackOverflow...",
            "Preguntando a la comunidad de Java...",
            "Aguanta no son enchiladas...",
            "Que como voy? pregunta el PM...",
            "No nos responsabilizamos por el resultado...",
            "Si lo sabes, para preguntas?...",
            "No se, pero parece que se esta quemando..."
    );

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

    @DeleteMapping("/entity/{id}")
    public ModelAndView deleteEntity(@PathVariable String id) {
        logger.info("Deleting entity with id: {}", id);
        return new ModelAndView("fragments/entity-deleted", Map.of("message", "Entidad con id: " + id + " borrada"));
    }

    @PostMapping("/sum-data")
    public ModelAndView sumData(@RequestParam int a, @RequestParam int b, @RequestParam(required = false) Integer waitTime,
        HttpServletResponse response) {
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
        response.addHeader("HX-Trigger", "sum-data-done");
        response.addHeader("HX-Trigger-After-Settle", "sum-data-done-after-settle");
        response.addHeader("HX-Trigger-After-Swap", "sum-data-done-after-swap");
        return new ModelAndView("fragments/sum-data", model);
    }

    @PostMapping("/got/died")
    public ModelAndView gotDied(@RequestParam String search) {
        logger.info("search: {}", search);

        List<Character> gotDeads = new ArrayList<>();

        gotDeads.add(new Character("Eddard Stark", "Season 1, Episode 9 (Baelor)"));
        gotDeads.add(new Character("Khal Drogo", "Season 1, Episode 10 (Fire and Blood)"));
        gotDeads.add(new Character("Renly Baratheon", "Season 2, Episode 5 (The Ghost of Harrenhal)"));
        gotDeads.add(new Character("Maester Luwin", "Season 2, Episode 10 (Valar Morghulis)"));
        gotDeads.add(new Character("Robb Stark", "Season 3, Episode 9 (The Rains of Castamere)"));
        gotDeads.add(new Character("Catelyn Stark", "Season 3, Episode 9 (The Rains of Castamere)"));
        gotDeads.add(new Character("Talisa Stark", "Season 3, Episode 9 (The Rains of Castamere)"));
        gotDeads.add(new Character("Joffrey Baratheon", "Season 4, Episode 2 (The Lion and the Rose)"));
        gotDeads.add(new Character("Oberyn Martell", "Season 4, Episode 8 (The Mountain and the Viper)"));
        gotDeads.add(new Character("Ygritte", "Season 4, Episode 9 (The Watchers on the Wall)"));
        gotDeads.add(new Character("Shae", "Season 4, Episode 10 (The Children)"));
        gotDeads.add(new Character("Tywin Lannister", "Season 4, Episode 10 (The Children)"));
        gotDeads.add(new Character("Mance Rayder", "Season 5, Episode 1 (The Wars to Come)"));
        gotDeads.add(new Character("Barristan Selmy", "Season 5, Episode 4 (Sons of the Harpy)"));
        gotDeads.add(new Character("Shireen Baratheon", "Season 5, Episode 9 (The Dance of Dragons)"));
        gotDeads.add(new Character("Selyse Baratheon", "Season 5, Episode 10 (Mother’s Mercy)"));
        gotDeads.add(new Character("Stannis Baratheon", "Season 5, Episode 10 (Mother’s Mercy)"));
        gotDeads.add(new Character("Myrcella Baratheon", "Season 5, Episode 10 (Mother’s Mercy)"));
        gotDeads.add(new Character("Ramsay Bolton", "Season 6, Episode 9 (Battle of the Bastards)"));
        gotDeads.add(new Character("Hodor", "Season 6, Episode 5 (The Door)"));
        gotDeads.add(new Character("Tommen Baratheon", "Season 6, Episode 10 (The Winds of Winter)"));
        gotDeads.add(new Character("Walder Frey", "Season 6, Episode 10 (The Winds of Winter)"));
        gotDeads.add(new Character("Obara and Nymeria Sand", "Season 7, Episode 2 (Stormborn)"));
        gotDeads.add(new Character("Olenna Tyrell", "Season 7, Episode 3 (The Queen's Justice)"));
        gotDeads.add(new Character("Petyr Baelish", "Season 7, Episode 7 (The Dragon and the Wolf)"));
        gotDeads.add(new Character("Viserion", "Season 7, Episode 6 (Beyond the Wall)"));
        gotDeads.add(new Character("The Night King", "Season 8, Episode 3 (The Long Night)"));
        gotDeads.add(new Character("Jorah Mormont", "Season 8, Episode 3 (The Long Night)"));
        gotDeads.add(new Character("Theon Greyjoy", "Season 8, Episode 3 (The Long Night)"));
        gotDeads.add(new Character("Melisandre", "Season 8, Episode 3 (The Long Night)"));
        gotDeads.add(new Character("Missandei", "Season 8, Episode 4 (The Last of the Starks)"));
        gotDeads.add(new Character("Varys", "Season 8, Episode 5 (The Bells)"));
        gotDeads.add(new Character("Euron Greyjoy", "Season 8, Episode 5 (The Bells)"));
        gotDeads.add(new Character("Cersei and Jaime Lannister", "Season 8, Episode 5 (The Bells)"));
        gotDeads.add(new Character("Daenerys Targaryen", "Season 8, Episode 6 (The Iron Throne)"));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Error", e); 
        }
        return new ModelAndView("fragments/got-died",
            Map.of("characters", gotDeads.stream().filter(predicate ->
                search.toLowerCase().equals("all") ||
                predicate.name().toLowerCase().contains(search.toLowerCase())).toList()));
    }

    /**
     * Starts the multiplication table calculation.
     * 
     * This method initiates a multiplication task with the given parameters. It generates a task ID based on the input numbers,
     * stores an initial status message, and executes the calculation asynchronously. The calculation may fail based on the provided
     * error probability. The result or error status is stored and can be retrieved later.
     * 
     * @param a the first number to multiply
     * @param b the second number to multiply
     * @param errorProbability the probability of an error occurring during the calculation (optional)
     * @return a ModelAndView object containing the initial status of the calculation and the task ID
     */
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

    /**
     * Retrieves the status of a multiplication table calculation.
     * 
     * This method checks the status of a multiplication task identified by the given task ID.
     * It randomly selects a status message from a predefined list if the task is still in progress.
     * If the task is completed or has encountered an error, it adds "HX-Trigger" header to the response.
     */
    @GetMapping("/multiply/progress")
    public ModelAndView multiplicationTableStatus(@RequestParam String taskId, HttpServletResponse response) {
        // randomly select a reason from reasones
        int index = ThreadLocalRandom.current().nextInt(0, STATUS_MESSAGES.size());
        String resultOrStatus = results.getOrDefault(taskId, STATUS_MESSAGES.get(index));
        if (isANumber(resultOrStatus) || resultOrStatus.equals("Error")) {
            response.addHeader("HX-Trigger", "multiply-" + taskId + "-done"); 
        }
        return new ModelAndView("fragments/multiply-progress",
            Map.of("status", STATUS_MESSAGES.get(index), "taskId", taskId));
    }

    @GetMapping("/multiply/done")
    public ModelAndView multiplicationTableDone(@RequestParam String taskId) {
        String result = results.getOrDefault(taskId, "Error");
        if (result.equals("Error")) {
            String[] values = taskId.split("x");
            return new ModelAndView("fragments/multiply-error",
                Map.of("status", result, "a", values[0], "b", values[1],
                "error", "Lo sentimos, no se pudo realizar la operacion"));
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

    @GetMapping("/next-steps")
    public ModelAndView nextSteps() {
        return new ModelAndView("fragments/next-steps");
    }

    @GetMapping("/categories/search")
    public ResponseEntity<String> searchCategories(@RequestParam String query) {
        List<String> categories = List.of(
            "Technology",
            "Science",
            "Health",
            "Education",
            "Sports", 
            "Entertainment",
            "Business",
            "Politics",
            "Art",
            "Music",
            "Movies",
            "TV Shows",
            "Books",
            "Travel",
            "Food",
            "Fashion",
            "Lifestyle",
            "Fitness",
            "Gaming",
            "Pets",
            "Home",
            "Gardening",
            "DIY",
            "Crafts",
            "Photography",
            "Cars",
            "Motorcycles",
            "Bicycles",
            "Planes",
            "Trains"
        );
        List<String> filteredCategories = categories.stream()
                .filter(category ->
                    query.toLowerCase().equals("all") || category.toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        String options = filteredCategories.stream()
                .map(category -> "<option value=\"" + category + "\">")
                .collect(Collectors.joining());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Error", e); 
        }
        return ResponseEntity.ok(options);
    }

    @PostMapping("/echo-input")
    public ResponseEntity<String> echoInput(@RequestParam String input, @RequestParam(required=false) Boolean sanitize) {
        if (sanitize != null && sanitize) {
            try {
                AntiSamy antiSamy = new AntiSamy();
                URL url = getClass().getResource("/antisamy.xml");
                CleanResults rs = antiSamy.scan(input, Policy.getInstance(url));
                input = rs.getCleanHTML();
            } catch (PolicyException | ScanException e) {
                logger.error("Error sanitizing input", e);
                return ResponseEntity.badRequest().body("<span style=\"color: red\">Error sanitizing input<span>");
            }
        }
        return ResponseEntity.ok(input);
    }

    @GetMapping("/multiple-fragments")
    public ModelAndView multipleFragments(@RequestParam String echo) {
        return new ModelAndView("fragments/multiple-fragments", Map.of("echo", echo));
    }
}

record Character(String name, String when) {
    public String toString() {
        return name + " - " + when;
    }
}
