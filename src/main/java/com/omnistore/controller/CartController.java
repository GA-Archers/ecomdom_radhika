package com.omnistore.controller;

import com.omnistore.dto.AddToCartRequestDto;
import com.omnistore.dto.CartResponseDto;
import com.omnistore.entity.User;
import com.omnistore.services.CartService;
import com.omnistore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(Principal principal, @RequestBody AddToCartRequestDto dto) {
        User user = userService.findUserByEmail(principal.getName());
        cartService.addToCart(user.getId(), dto);
        return ResponseEntity.ok(cartService.getCartByUserId(user.getId()));
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return ResponseEntity.ok(cartService.getCartByUserId(user.getId()));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeFromCart(Principal principal, @PathVariable Long productId) {
        User user = userService.findUserByEmail(principal.getName());
        cartService.removeFromCart(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }
}
