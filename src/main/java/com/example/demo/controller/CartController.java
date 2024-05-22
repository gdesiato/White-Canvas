package com.example.demo.controller;

import com.example.demo.model.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.service.CartService;
import com.example.demo.model.Cart;
import com.example.demo.model.User;
import com.example.demo.service.ServicesService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
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


    // Double-check that the cartService is properly injected
    @PostConstruct
    public void init() {
        System.out.println("0000000000000000000000000000");
        System.out.println("cartService: " + cartService);
    }


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
////        if (user == null) {
////            // Redirect to the error-and-login page if no user is authenticated
////            return "error-and-login";
////        }
        Cart cart = cartService.getShoppingCartForUser(user.getUsername());
        model.addAttribute("cart", cart);
        if (cart == null) {
            return "no-cart";
        }
        return "cart";
    }

//    @PostMapping("/new/{customerId}")
//    @Transactional
//    public String createNewCart(@PathVariable Long customerId, Model model) {
//        Cart cart = cartService.createCart(customerId);
//        model.addAttribute("cart", cart);
//        return "new-cart";
//    }

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
                            Model model,
                            HttpSession session) {

        //Checking the session ID
        System.out.println("----------->>>>>>> Session ID when adding to cart: " + session.getId());

        // Debugging code before the operation
        System.out.println("Attempting to add to cart with the following parameters:");
        System.out.println("userId: " + userId);
        System.out.println("serviceName: " + serviceName);
        System.out.println("quantity: " + quantity);

        if (quantity <= 0) {
            return "error";
        }

        Cart cart = cartService.addToCart(userId, serviceName, quantity);

        // Debugging code after the operation
        System.out.println("After the operation:");
        if (cart == null) {
            System.out.println("The cart is null. The operation might have failed.");
            return "error";
        } else {
            System.out.println("Cart items: " + cart.getCartItems());
        }

        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/remove/{itemId}")
    @Transactional
    public String removeItemFromCart(@PathVariable Long itemId,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);

        if (user == null) {
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

        System.out.println("/././././././././././././/./././././/./");
        System.out.println("Removed item with ID: " + itemId + " from cart");

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
