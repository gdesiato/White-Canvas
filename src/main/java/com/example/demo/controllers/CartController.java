package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.User;
import com.example.demo.repositories.CartItemRepository;
import com.example.demo.services.CartService;
import com.example.demo.services.ServicesService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private CartItemRepository cartItemRepository;


    @GetMapping
    @Transactional
    public String getCart(@AuthenticationPrincipal UserDetails userPrincipal, Model model) {
        String username = userPrincipal.getUsername();
        User user = userService.findByUsername(username);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("User: " + user);  // print user to console

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
//        if (user == null) {
//            // Redirect to the error-and-login page if no user is authenticated
//            return "error-and-login";
//        }
        Cart cart = cartService.getShoppingCartForUser(user.getUsername());
        model.addAttribute("cart", cart);
        if (cart == null) {
            return "no-cart";
        }
        return "cart";
    }

    @PostMapping("/new/{customerId}")
    @Transactional
    public String createNewCart(@PathVariable Long customerId, Model model) {
        Cart cart = cartService.createCart(customerId);
        model.addAttribute("cart", cart);
        return "new-cart";
    }

    @GetMapping("/{userId}")
    public String viewCart(@PathVariable("userId") Long userId, Model model) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return "error";
        }

        Cart cart = user.getCart();

        model.addAttribute("cart", cart);

        return "cart";
    }

    @PostMapping("/{userId}")
    @Transactional
    public String addToCart(@PathVariable("userId") Long userId,
                            @RequestParam("serviceName") String serviceName,
                            @RequestParam("quantity") Integer quantity,
                            Model model) {

        Cart cart = cartService.addToCart(userId, serviceName, quantity);

        if (cart == null) {
            return "error";
        }

        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/{userId}/items")
    @Transactional
    public String addItemToCart(@PathVariable("userId") Long userId,
                                @RequestParam("serviceName") String serviceName,
                                @RequestParam("quantity") Integer quantity,
                                Model model) {

        Cart cart = cartService.addToCart(userId, serviceName, quantity);

        if (cart == null) {
            return "error";
        }

        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/remove/{itemId}")
    @Transactional
    public String removeItemFromCart(@PathVariable Long itemId, @AuthenticationPrincipal User user, Model model) {
        if (user == null) {
            // Add logic to handle unauthenticated user. For example, redirect to login page.
            return "redirect:/login";
        }

        Cart cart = cartService.getShoppingCartForUser(user.getUsername());
        if (cart == null) {
            return "error";
        }

        CartItem cartItem = cartItemRepository.findById(itemId).orElse(null);
        if (cartItem == null) {
            return "error";
        }

        cartService.removeItemFromCart(cart, cartItem);
        model.addAttribute("cart", cart);
        return "redirect:/cart/" + user.getId();
    }

    @PostMapping("/clear")
    @Transactional
    public String clearCart(@AuthenticationPrincipal User user, Model model) {
        Cart cart = cartService.getShoppingCartForUser(user.getUsername());
        if (cart == null) {
            return "error";
        }

        cartService.clearCart(cart);
        model.addAttribute("cart", cart);
        return "redirect:/cart" + user.getId();
    }

    @GetMapping("/{cartId}/total")
    @Transactional
    public String getTotalPrice(@PathVariable Long cartId, Model model) {
        Cart cart = cartService.getCartById(cartId);
        if (cart == null) {
            return "error";
        }
        double totalPrice = cart.getTotalPrice();
        model.addAttribute("totalPrice", totalPrice);
        return "totalPrice";
    }

}
