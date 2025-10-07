package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.ProductCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.ProductUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.ProductResponse;
import com.BTL_JAVA.BTL.Entity.Product.Category;
import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Entity.Product.ProductVariation;
import com.BTL_JAVA.BTL.Repository.CategoryRepository;
import com.BTL_JAVA.BTL.Repository.ProductRepository;
import com.BTL_JAVA.BTL.Repository.ProductVariationRepository;
import com.BTL_JAVA.BTL.Service.Cloudinary.UploadImageFile;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductVariationRepository productVariationRepository;
    UploadImageFile uploadImageFile;

    @Transactional
    public ApiResponse<ProductResponse> create(ProductCreationRequest req) throws IOException {
        Product p = Product.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .price(req.getPrice())
                .build();

        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            p.setCategory(cat);
        }

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = uploadImageFile.uploadImage(req.getImage());
            p.setImage(url);
        }

        Product saved = productRepository.save(p);

        Set<Integer> vIds = req.getVariationIds() == null ? Set.of() : req.getVariationIds();
        if (!vIds.isEmpty()) {
            var variations = productVariationRepository.findAllById(vIds);
            var found = variations.stream().map(ProductVariation::getId).collect(Collectors.toSet());
            var missing = vIds.stream().filter(i -> !found.contains(i)).collect(Collectors.toSet());
            if (!missing.isEmpty()) {
                throw new RuntimeException("Variation không tồn tại: " + missing);
            }
            variations.forEach(v -> v.setProduct(saved));
            productVariationRepository.saveAll(variations);
        }

        return ApiResponse.<ProductResponse>builder()
                .code(1000).message("Success")
                .result(toResponse(saved, vIds))
                .build();
    }


    @Transactional
    public ApiResponse<ProductResponse> update(Integer id, ProductUpdateRequest req) throws IOException {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (req.getTitle() != null)       p.setTitle(req.getTitle());
        if (req.getDescription() != null) p.setDescription(req.getDescription());
        if (req.getPrice() != null)       p.setPrice(req.getPrice());

        if (req.getCategoryId() != null) {
            Category cat = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            p.setCategory(cat);
        }

        if (req.getImage() != null && !req.getImage().isEmpty()) {
            String url = uploadImageFile.uploadImage(req.getImage());
            p.setImage(url);
        }


        if (req.getAddVariationIds() != null && !req.getAddVariationIds().isEmpty()) {
            var toAdd = productVariationRepository.findAllById(req.getAddVariationIds());
            var found = toAdd.stream().map(ProductVariation::getId).collect(Collectors.toSet());
            var missing = req.getAddVariationIds().stream().filter(i -> !found.contains(i)).toList();
            if (!missing.isEmpty()) {
                throw new RuntimeException("Variation không tồn tại: " + missing);
            }
            toAdd.forEach(v -> v.setProduct(p));
            productVariationRepository.saveAll(toAdd);
        }

        // BỚT variation (detach khỏi product này)
        if (req.getRemoveVariationIds() != null && !req.getRemoveVariationIds().isEmpty()) {
            var toRemove = productVariationRepository.findAllById(req.getRemoveVariationIds());
            toRemove.stream()
                    .filter(v -> v.getProduct() != null && v.getProduct().getProductId() == p.getProductId())
                    .forEach(v -> v.setProduct(null));
            productVariationRepository.saveAll(toRemove);
        }

        Product saved = productRepository.save(p);

        Set<Integer> currentVarIds = (saved.getProductVariations() == null) ? Set.of()
                : saved.getProductVariations().stream().map(ProductVariation::getId).collect(Collectors.toSet());

        return ApiResponse.<ProductResponse>builder()
                .code(1000).message("Success")
                .result(toResponse(saved, currentVarIds))
                .build();
    }

    @Transactional
    public ApiResponse<Void> delete(Integer id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        if (p.getProductVariations() != null && !p.getProductVariations().isEmpty()) {
            throw new RuntimeException("Không thể xoá: vẫn còn biến thể (product_variation) thuộc sản phẩm này");
        }

        productRepository.delete(p);
        return ApiResponse.<Void>builder()
                .code(1000).message("Deleted").result(null).build();
    }


    public ApiResponse<ProductResponse> get(Integer id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Set<Integer> varIds = (p.getProductVariations() == null) ? Set.of()
                : p.getProductVariations().stream().map(ProductVariation::getId).collect(Collectors.toSet());

        return ApiResponse.<ProductResponse>builder()
                .code(1000).message("Success")
                .result(toResponse(p, varIds))
                .build();
    }


    public ApiResponse<List<ProductResponse>> list() {
        var all = productRepository.findAll();

        var data = all.stream().map(p -> {
            Set<Integer> varIds = (p.getProductVariations() == null) ? Set.of()
                    : p.getProductVariations().stream().map(ProductVariation::getId).collect(Collectors.toSet());
            return toResponse(p, varIds);
        }).toList();

        return ApiResponse.<List<ProductResponse>>builder()
                .code(1000).message("Success")
                .result(data)
                .build();
    }

    private ProductResponse toResponse(Product p, Set<Integer> variationIds) {
        Set<Integer> vIds = (variationIds != null) ? variationIds :
                (p.getProductVariations() == null ? Set.of()
                        : p.getProductVariations().stream().map(ProductVariation::getId).collect(Collectors.toSet()));

        return ProductResponse.builder()
                .productId(p.getProductId())
                .title(p.getTitle())
                .description(p.getDescription())
                .price(p.getPrice())
                .image(p.getImage())
                .categoryId(p.getCategory() == null ? null : p.getCategory().getId())
                .variationCount(vIds.size())
                .variationIds(vIds)
                .build();
    }
}
