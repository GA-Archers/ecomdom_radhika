package com.omnistore.controller;

import com.omnistore.dto.ReviewRequestDto;
import com.omnistore.dto.ReviewResponseDto;
import com.omnistore.entity.ProductReview;
import com.omnistore.entity.User;
import com.omnistore.services.ReviewService;
import com.omnistore.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(@RequestBody ReviewRequestDto request, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        ProductReview review = reviewService.addReview(user.getId(), request.getProductId(), request.getRating(), request.getComment());
        return ResponseEntity.ok(mapToDto(review));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProduct(@PathVariable Long productId) {
        List<ProductReview> reviews = reviewService.getReviewsByProduct(productId);
        List<ReviewResponseDto> responseDtos = reviews.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    private ReviewResponseDto mapToDto(ProductReview review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getEmail());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
