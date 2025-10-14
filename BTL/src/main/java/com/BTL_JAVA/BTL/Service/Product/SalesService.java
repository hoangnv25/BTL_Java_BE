package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.SalesCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.SalesUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Request.ProductSaleItemRequest;
import com.BTL_JAVA.BTL.DTO.Response.SalesResponse;
import com.BTL_JAVA.BTL.DTO.Response.ProductSaleItemResponse;
import com.BTL_JAVA.BTL.Entity.Product.Sales;
import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Entity.Product.ProductSale;
import com.BTL_JAVA.BTL.Repository.SalesRepository;
import com.BTL_JAVA.BTL.Repository.ProductRepository;
import com.BTL_JAVA.BTL.Repository.ProductSaleRepository;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesService {

    SalesRepository salesRepository;
    ProductRepository productRepository;
    ProductSaleRepository productSaleRepository;

    public ApiResponse<List<SalesResponse>> getAllSales(Boolean active) {
        try {
            List<Sales> allSales = salesRepository.findAll();

            List<SalesResponse> salesResponses = allSales.stream()
                    .map(sale -> {
                        boolean isActive = calculateActiveStatus(sale.getStDate(), sale.getEndDate());
                        return toResponse(sale, isActive);
                    })
                    .collect(Collectors.toList());

            if (active != null) {
                salesResponses = salesResponses.stream()
                        .filter(sale -> sale.getActive().equals(active))
                        .collect(Collectors.toList());
            }

            return ApiResponse.<List<SalesResponse>>builder()
                    .result(salesResponses)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.SALES_NOT_FOUND);
        }
    }

    @Transactional
    public ApiResponse<SalesResponse> create(SalesCreationRequest request) {
        try {
            validateSaleDates(request.getStDate(), request.getEndDate());

            boolean isActive = calculateActiveStatus(request.getStDate(), request.getEndDate());

            Sales sale = Sales.builder()
                    .name(request.getName())
                    .description(request.getDescription() != null ? request.getDescription() : "") // XỬ LÝ NULL
                    .stDate(request.getStDate())
                    .endDate(request.getEndDate())
                    .active(isActive)
                    .build();

            Sales saved = salesRepository.save(sale);

            return ApiResponse.<SalesResponse>builder()
                    .result(toResponse(saved, isActive))
                    .build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_FAILED);
        }
    }

    @Transactional
    public ApiResponse<SalesResponse> update(Integer id, SalesUpdateRequest request) {
        try {
            Sales sale = salesRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.SALE_NOT_EXISTED));

            // UPDATE THÔNG TIN SALE
            if (request.getName() != null) sale.setName(request.getName());
            if (request.getDescription() != null) sale.setDescription(request.getDescription());
            if (request.getStDate() != null) sale.setStDate(request.getStDate());
            if (request.getEndDate() != null) sale.setEndDate(request.getEndDate());

            // VALIDATE DATES
            if (request.getStDate() != null && request.getEndDate() != null) {
                validateSaleDates(request.getStDate(), request.getEndDate());
            } else if (request.getStDate() != null) {
                validateSaleDates(request.getStDate(), sale.getEndDate());
            } else if (request.getEndDate() != null) {
                validateSaleDates(sale.getStDate(), request.getEndDate());
            }

            // TÍNH LẠI ACTIVE STATUS
            LocalDateTime newStDate = request.getStDate() != null ? request.getStDate() : sale.getStDate();
            LocalDateTime newEndDate = request.getEndDate() != null ? request.getEndDate() : sale.getEndDate();
            boolean isActive = calculateActiveStatus(newStDate, newEndDate);
            sale.setActive(isActive);

            Sales saved = salesRepository.save(sale);

            // XỬ LÝ PRODUCTS VỚI VALUE RIÊNG
            if (request.getProducts() != null && !request.getProducts().isEmpty()) {
                updateProductsInSale(saved, request.getProducts());
            }

            return ApiResponse.<SalesResponse>builder()
                    .result(toResponse(saved, isActive))
                    .build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.UPDATE_FAILED);
        }
    }

    @Transactional
    public ApiResponse<Void> delete(Integer id) {
        try {
            Sales sale = salesRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.SALE_NOT_EXISTED));

            // XÓA TẤT CẢ ProductSale TRƯỚC KHI XÓA SALE
            if (!sale.getProductSales().isEmpty()) {
                productSaleRepository.deleteAll(sale.getProductSales());
            }

            salesRepository.delete(sale);

            return ApiResponse.<Void>builder()
                    .result(null)
                    .build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }

    private void updateProductsInSale(Sales sale, List<ProductSaleItemRequest> productItems) {
        // XÓA TẤT CẢ PRODUCTS CŨ
        productSaleRepository.deleteAll(sale.getProductSales());
        sale.getProductSales().clear();

        // THÊM PRODUCTS MỚI VỚI VALUE RIÊNG
        for (ProductSaleItemRequest item : productItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

            // VALIDATE SALE VALUE
            validateSaleValue(item.getValue());

            ProductSale productSale = ProductSale.builder()
                    .sale(sale)
                    .product(product)
                    .saleValue(item.getValue())
                    .build();

            productSaleRepository.save(productSale);
            sale.getProductSales().add(productSale);
        }
    }

    private boolean calculateActiveStatus(LocalDateTime stDate, LocalDateTime endDate) {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(stDate) && !now.isAfter(endDate);
    }

    private void validateSaleDates(LocalDateTime stDate, LocalDateTime endDate) {
        if (endDate.isBefore(stDate)) {
            throw new AppException(ErrorCode.INVALID_SALE_DATE);
        }
    }

    private void validateSaleValue(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(new BigDecimal("0.99")) > 0) {
            throw new AppException(ErrorCode.INVALID_SALE_VALUE);
        }
    }

    private SalesResponse toResponse(Sales sales, boolean isActive) {
        // XỬ LÝ AN TOÀN CHO SALE MỚI TẠO (CHƯA CÓ PRODUCTS)
        List<ProductSaleItemResponse> productItems = (sales.getProductSales() == null || sales.getProductSales().isEmpty())
                ? List.of()  // TRẢ VỀ MẢNG RỖNG CHO SALE MỚI
                : sales.getProductSales().stream()
                .map(productSale -> {
                    // KIỂM TRA NULL CHO product VÀ saleValue
                    if (productSale.getProduct() == null || productSale.getSaleValue() == null) {
                        return ProductSaleItemResponse.builder()
                                .id(0)
                                .value(BigDecimal.ZERO)
                                .image("")
                                .build();
                    }
                    return ProductSaleItemResponse.builder()
                            .id(productSale.getProduct().getProductId())
                            .value(productSale.getSaleValue())
                            .image(productSale.getProduct().getImage() != null
                                    ? productSale.getProduct().getImage()
                                    : "")
                            .build();
                })
                .collect(Collectors.toList());

        return SalesResponse.builder()
                .id(sales.getId())
                .name(sales.getName())
                .description(sales.getDescription() != null ? sales.getDescription() : "")
                .stDate(sales.getStDate())
                .endDate(sales.getEndDate())
                .active(isActive)
                .list_product(productItems)
                .build();
    }
}