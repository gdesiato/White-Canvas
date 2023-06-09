package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(){
        return "home";
    }

    // NAVIGATION BAR

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/explore")
    public String explore() {
        return "explore";
    }

    // Testing
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Test successful";
    }
}
