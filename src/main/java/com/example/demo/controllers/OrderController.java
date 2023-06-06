package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.models.CartItem;
import com.example.demo.models.Order;
import com.example.demo.services.CartService;
import com.example.demo.services.OrderService;
import com.example.demo.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/order-confirmation")
    public String showOrderConfirmation(@RequestParam("orderId") Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order-confirmation";
    }

    @PostMapping("/create")
    public String createOrder(RedirectAttributes redirectAttributes, HttpSession session) {

        //Checking the session ID
        System.out.println("----------->>>>>>> Session ID when adding to cart: " + session.getId());

        String username = userService.getCurrentUsername();
        System.out.println("++++++++++++++++++++++");
        System.out.println("Username: " + username);

        if (username == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getShoppingCartForUser(username);
        System.out.println("++++++++++++++++++++++");
        System.out.println("Cart: " + cart);

        if (cart == null || cart.getCartItems().isEmpty()) {
            return "redirect:/cart";
        }

        System.out.println("++++++++++++++++++++++");
        System.out.println("Cart Items: ");
        for (CartItem item : cart.getCartItems()) {
            System.out.println("Item: " + item);
        }

        Order order = orderService.createOrderFromCart(cart);
        cartService.clearCart(cart);

        redirectAttributes.addAttribute("orderId", order.getId());
        return "redirect:/order/order-confirmation";
    }
}

