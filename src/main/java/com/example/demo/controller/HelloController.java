package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class HelloController {

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/hello2")
    public String hello2() {
        return "hello";
    }

    @GetMapping("/introduce")
    public String introduce(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "introduce";
    }

    @ResponseBody
    @GetMapping("/json")
    public Map<String, Object> json() {
        return Map.of("age", 26, "name", "허준기");
    }
}