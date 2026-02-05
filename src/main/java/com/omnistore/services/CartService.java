package com.omnistore.services;

import com.omnistore.dto.AddToCartRequestDto;
import com.omnistore.dto.CartResponseDto;
import com.omnistore.dto.CartItemResponseDto;
import com.omnistore.entity.Cart;
import com.omnistore.entity.CartItem;
import com.omnistore.entity.Product;
import com.omnistore.exception.ResourceNotFoundException;
import com.omnistore.repository.CartRepository;
import com.omnistore.repository.CartItemRepository;
import com.omnistore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public Cart addToCart(Long userId, AddToCartRequestDto dto) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());

        cart.getItems().add(item);
        cartItemRepository.save(item);

        return cart;
    }

    public CartResponseDto getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        CartResponseDto response = new CartResponseDto();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUserId());

        AtomicReference<Double> total = new AtomicReference<>(0.0);

        List<CartItemResponseDto> items = cart.getItems().stream().map(item -> {
            CartItemResponseDto dto = new CartItemResponseDto();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            total.updateAndGet(v -> v + item.getProduct().getPrice() * item.getQuantity());
            return dto;
        }).toList();

        response.setItems(items);
        response.setTotalAmount(total.get());

        return response;
    }

    public void removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user ID: " + userId));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart with ID: " + productId));

        cart.getItems().remove(itemToRemove);
        cartItemRepository.delete(itemToRemove);
        cartRepository.save(cart);
    }
}
