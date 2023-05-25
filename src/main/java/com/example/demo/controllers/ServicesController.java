package com.example.demo.controllers;

import com.example.demo.models.Services;
import com.example.demo.repositories.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/services")
public class ServicesController {

    @Autowired
    private ServicesRepository servicesRepository;

    @GetMapping
    public String getAllServices(Model model) {
        List<Services> services = servicesRepository.findAll();
        model.addAttribute("services", services);
        return "services";
    }

    @GetMapping("/{id}")
    public String getService(@PathVariable Long id, Model model) {
        Services service = servicesRepository.findById(id).orElse(null);
        model.addAttribute("service", service);
        return "service";
    }
}
