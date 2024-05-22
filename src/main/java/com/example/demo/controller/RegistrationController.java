package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/new")
    public String showNewUserPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam("password-repeat") String confirmPassword, Model model) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            return "username-exists";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            return "password-mismatch";
        }

        Role userRole = roleRepository.findRoleByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
        user.setRoles(Collections.singleton(userRole));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Create a new Cart for the User
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        userService.saveUser(user);

        model.addAttribute("username", user.getUsername());

        return "registration-confirmation";
    }

}





