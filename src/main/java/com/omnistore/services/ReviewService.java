package com.omnistore.services;

import com.omnistore.entity.Product;
import com.omnistore.entity.ProductReview;
import com.omnistore.entity.User;
import com.omnistore.exception.BadRequestException;
import com.omnistore.exception.ResourceNotFoundException;
import com.omnistore.repository.ProductRepository;
import com.omnistore.repository.ProductReviewRepository;
import com.omnistore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ProductReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ProductReviewRepository reviewRepository,
                         ProductRepository productRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProductReview addReview(Long userId, Long productId, Integer rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }

        ProductReview review = new ProductReview();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    public List<ProductReview> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
