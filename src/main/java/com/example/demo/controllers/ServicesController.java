package com.example.demo.controllers;

import com.example.demo.models.Services;
import com.example.demo.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/services")
public class ServicesController {

    @Autowired
    private ServiceRepository servicesRepository;

    @GetMapping
    public String getAllServices(Model model) {
        List<Services> services = servicesRepository.findAll();
        model.addAttribute("services", services);
        return "services";
    }

    @GetMapping("/new")
    public String showAddServiceForm(Model model) {
        Services service = new Services();
        model.addAttribute("service", service);
        return "add-service";
    }

    @PostMapping
    public String addService(@ModelAttribute("service") Services service) {
        servicesRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/{id}")
    public String getService(@PathVariable Long id, Model model) {
        Services service = servicesRepository.findById(id).orElse(null);
        model.addAttribute("service", service);
        return "service";
    }


    @GetMapping("/{id}/update")
    public String showUpdateServiceForm(@PathVariable Long id, Model model) {
        Services service = servicesRepository.findById(id).orElse(null);
        model.addAttribute("service", service);
        return "update-service";
    }

    @PostMapping("/{id}/update")
    public String updateService(@PathVariable Long id, @ModelAttribute("service") Services updatedService) {
        Services service = servicesRepository.findById(id).orElse(null);
        if (service != null) {
            service.setServiceName(updatedService.getServiceName());
            service.setDescription(updatedService.getDescription());
            service.setCost(updatedService.getCost());
            servicesRepository.save(service);
        }
        return "redirect:/services";
    }

    @RequestMapping(value = "/{id}/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteService(@PathVariable Long id) {
        servicesRepository.deleteById(id);
        return "redirect:/services";
    }
}
