package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.User;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController implements ErrorController {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CartService cartService;


    @GetMapping("/list")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/user/list";
    }

    @GetMapping
    public String getUser(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("username", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        return "user-view";
    }

    @GetMapping("/update/{id}")
    public String showUpdateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "update-user";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/user/list";
    }

    @GetMapping("/dashboard")
    public String showUserDashboard(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);

            if (user != null) {
                model.addAttribute("user", user);

                // Check if the user has an admin role
                boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ADMIN"));

                if (isAdmin) {
                    // Redirect to the admin dashboard if the user is an admin
                    return "redirect:/admin/dashboard";
                } else {
                    // Handle the user dashboard
                    Cart userCart = cartService.findCartByUser(user);

                    if (userCart == null) {
                        userCart = new Cart();
                        userCart.setUser(user);
                        userCart = cartService.saveCart(userCart);
                    }

                    List<CartItem> items = List.of(userCart.getItems());

                    model.addAttribute("cart", userCart);
                    model.addAttribute("items", items);
                }
            }
        }
        return "user-dashboard-frag";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Principal principal, Model model) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);

            if (user != null) {
                Cart userCart = cartService.findCartByUser(user);

                if (userCart == null) {
                    return "redirect:/user/dashboard";
                }

                List<CartItem> items = List.of(userCart.getItems());
                model.addAttribute("cart", userCart);
                model.addAttribute("items", items);
                model.addAttribute("user", user);

                return "checkout";
            }
        }
        return "redirect:/login";
    }

}
