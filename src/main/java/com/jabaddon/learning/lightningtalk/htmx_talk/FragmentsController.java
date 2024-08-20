package com.jabaddon.learning.lightningtalk.htmx_talk;

import java.util.Map;

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

    @GetMapping("/basics/1")
    public String basics1() {
        return "fragments/basics1";
    }

    @PostMapping("/basics/2")
    public ModelAndView basics2(@RequestParam String name, @RequestParam String lastName) {
        return new ModelAndView("fragments/basics2", Map.of("name", name, "lastName", lastName));
    }

    @PostMapping("/basics/3")
    public ModelAndView basics3(@RequestParam String name, @RequestParam String lastName, @RequestHeader("Authentication") String token) {
        if (!"123445".equals(token)) {
            return new ModelAndView("fragments/basics3", Map.of("error", "Token is required"));
        }
        return new ModelAndView("fragments/basics3", Map.of("name", name, "lastName", lastName));
    }
}

record Person(String name, String lastName) {
}
