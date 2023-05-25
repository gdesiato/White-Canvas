package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.models.User;
import com.example.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        User user = (User) principal;
        Cart cart = cartService.getCurrentCart(user);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("itemId") Long itemId, @RequestParam("quantity") int quantity, Principal principal) {
        User user = (User) principal;
        cartService.addItemToCart(user, itemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("itemId") Long itemId, Principal principal) {
        User user = (User) principal;
        cartService.removeItemFromCart(user, itemId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(Principal principal) {
        User user = (User) principal;
        cartService.clearCart(user);
        return "redirect:/cart";
    }
}
