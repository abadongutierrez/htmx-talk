package com.jabaddon.learning.lightningtalk.htmx_talk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

@Controller
@RequestMapping("/fragments")
public class FragmentsController {
    private static final Logger logger = LoggerFactory.getLogger(FragmentsController.class);

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
