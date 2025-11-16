package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.Product.CartItemRequest;
import com.BTL_JAVA.BTL.DTO.Request.Product.UpdateQuantityRequest;
import com.BTL_JAVA.BTL.DTO.Response.Product.CartItemResponse;
import com.BTL_JAVA.BTL.Entity.Product.Cart;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import com.BTL_JAVA.BTL.Entity.User;
import com.BTL_JAVA.BTL.Repository.CartRepository;
import com.BTL_JAVA.BTL.Repository.ProductVariationRepository;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import com.BTL_JAVA.BTL.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {

    CartRepository cartRepository;
    ProductVariationRepository productVariationRepository;
    UserRepository userRepository;

    public ApiResponse<List<CartItemResponse>> getCart() {
        try {
            User user = getCurrentUser();
            List<Cart> cartItems = cartRepository.findAll().stream()
                    .filter(cart -> cart.getUser().getId() == user.getId())
                    .collect(Collectors.toList());

            List<CartItemResponse> items = toCartItemResponseList(cartItems);

            return ApiResponse.<List<CartItemResponse>>builder()
                    .result(items)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.CART_NOT_FOUND);
        }
    }

    public ApiResponse<List<CartItemResponse>> addToCart(CartItemRequest request) {
        try {
            log.info("=== START ADD TO CART ===");
            User user = getCurrentUser();
            log.info("User found: ID={}, Name={}", user.getId(), user.getFullName());

            log.info("Request data: product_variation_id={}, quantity={}",
                    request.getProduct_variation_id(), request.getQuantity());

            if (request.getQuantity() <= 0) {
                log.error("Invalid quantity: {}", request.getQuantity());
                throw new AppException(ErrorCode.INVALID_QUANTITY);
            }

            // Tìm product variation
            log.info("Looking for product variation with ID: {}", request.getProduct_variation_id());
            ProductVariation variation = productVariationRepository.findById(request.getProduct_variation_id())
                    .orElseThrow(() -> {
                        log.error("Product variation not found: {}", request.getProduct_variation_id());
                        return new AppException(ErrorCode.PRODUCT_VARIATION_NOT_FOUND);
                    });

            // Tìm cart item hiện tại
            log.info("Looking for existing cart item...");
            Cart existingCart = findCartItemByUserAndVariation(user, variation);
            log.info("Existing cart item: {}", existingCart != null ? "FOUND ID=" + existingCart.getId() : "NOT FOUND");

            int totalQuantity = request.getQuantity();
            if (existingCart != null) {
                totalQuantity += existingCart.getQuantity();
                log.info("Existing quantity: {}, New quantity: {}, Total: {}",
                        existingCart.getQuantity(), request.getQuantity(), totalQuantity);
            }

            // Kiểm tra stock
            log.info("Stock quantity: {}, Required quantity: {}", variation.getStockQuantity(), totalQuantity);
            if (totalQuantity > variation.getStockQuantity()) {
                log.error("Not enough stock");
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
            }

            // Lưu cart
            if (existingCart != null) {
                log.info("Updating existing cart item...");
                existingCart.setQuantity(totalQuantity);
                Cart savedCart = cartRepository.save(existingCart);
                log.info("Cart item updated successfully: ID={}", savedCart.getId());
            } else {
                log.info("Creating new cart item...");
                Cart newCart = Cart.builder()
                        .user(user)
                        .productVariation(variation)
                        .quantity(totalQuantity)
                        .build();
                Cart savedCart = cartRepository.save(newCart);
                log.info("Cart item created successfully: ID={}", savedCart.getId());
            }

            log.info("=== END ADD TO CART - SUCCESS ===");
            return getCart();

        } catch (AppException e) {
            log.error("AppException in addToCart: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("UNEXPECTED ERROR in addToCart: {}", e.getMessage(), e);
            e.printStackTrace(); // In full stack trace
            throw new AppException(ErrorCode.CART_UPDATE_FAILED);
        }
    }


    public ApiResponse<List<CartItemResponse>> updateCartItem(Integer productVariationId, UpdateQuantityRequest request) {
        try {
            User user = getCurrentUser();
            int quantity = request.getQuantity();

            if (quantity < 0) {
                throw new AppException(ErrorCode.INVALID_QUANTITY);
            }

            ProductVariation variation = productVariationRepository.findById(productVariationId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIATION_NOT_FOUND));

            if (quantity > 0 && quantity > variation.getStockQuantity()) {
                throw new AppException(ErrorCode.NOT_ENOUGH_STOCK);
            }

            Cart existingCart = findCartItemByUserAndVariation(user, variation);

            if (existingCart != null) {
                if (quantity == 0) {
                    cartRepository.delete(existingCart);
                } else {
                    existingCart.setQuantity(quantity);
                    cartRepository.save(existingCart);
                }
            } else if (quantity > 0) {
                Cart newCart = Cart.builder()
                        .user(user)
                        .productVariation(variation)
                        .quantity(quantity)
                        .build();
                cartRepository.save(newCart);
            }

            return getCart();

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CART_UPDATE_FAILED);
        }
    }


    public ApiResponse<List<CartItemResponse>> removeFromCart(Integer productVariationId) {
        try {
            User user = getCurrentUser();

            ProductVariation variation = productVariationRepository.findById(productVariationId)
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIATION_NOT_FOUND));

            Cart existingCart = findCartItemByUserAndVariation(user, variation);

            if (existingCart != null) {
                cartRepository.delete(existingCart);
            }

            return getCart();

        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CART_UPDATE_FAILED);
        }
    }

    private User getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(userId)).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        return user;
    }

    private Cart findCartItemByUserAndVariation(User user, ProductVariation variation) {
        return cartRepository.findAll().stream()
                .filter(cart -> cart.getUser().getId() == user.getId() &&
                        cart.getProductVariation().getId() == variation.getId())
                .findFirst()
                .orElse(null);
    }

    private List<CartItemResponse> toCartItemResponseList(List<Cart> cartItems) {
        return cartItems.stream()
                .map(cart -> CartItemResponse.builder()
                        .product_id(cart.getProductVariation().getProduct().getProductId())
                        .product_variation_id(cart.getProductVariation().getId())
                        .quantity(cart.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }
}