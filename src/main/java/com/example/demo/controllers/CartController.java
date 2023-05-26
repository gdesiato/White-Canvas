package com.example.demo.controllers;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.Services;
import com.example.demo.models.User;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String viewCart(Model model, Principal principal) {
        User user = (User) principal;
        Cart cart = cartService.getCurrentCart(user);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public void addItemToCart(User user, Long serviceId, int quantity) {
        // Ensure the user is persisted
        User persistedUser = userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + user.getId()));

        Cart cart = cartService.getCurrentCart(persistedUser);
        if(cart != null) {
            CartItem cartItem = cartItemRepository.findCartItemByIdAndCart(serviceId, cart);
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                Services service = servicesService.getServiceById(serviceId);
                cartItem = new CartItem(service, quantity);
                cart.getCartItems().add(cartItem);
            }
            // Saving cart with updated cart items
            cartRepository.save(cart);
        }
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
