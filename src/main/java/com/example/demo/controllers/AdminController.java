package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);



//    @GetMapping
//    public String getUser(Model model, Authentication authentication) {
//        User admin = (User) authentication.getPrincipal();
//        model.addAttribute("username", admin.getUsername());
//        model.addAttribute("roles", admin.getAuthorities());
//        return "admin-view";
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String getUser(Model model, Authentication authentication) {

        logger.info("Accessing getUser method");
        User admin = (User) authentication.getPrincipal();
        logger.info("Authenticated user: {}", admin.toString());
        model.addAttribute("username", admin.getUsername());
        model.addAttribute("roles", admin.getAuthorities());
        return "admin-view";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model, String username) {
        User user = userService.getUser(username);
        model.addAttribute("user", user);
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "admin-dashboard";
    }

}
