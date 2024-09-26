package com.jabaddon.learning.lightningtalk.htmx_talk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/pages/basics")
    public String basics() {
        return "basics";
    }
}
