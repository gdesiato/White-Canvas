package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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


    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String getUser(Model model, Authentication authentication) {

        logger.info("Accessing getUser method");

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.info("Authenticated user: {}", userDetails.getUsername());

            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("roles", userDetails.getAuthorities());
        }

        return "admin-view";
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model, String username) {
        User user = userService.getUser(username);
        model.addAttribute("user", user);
        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        return "admin-dashboard";
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

}
