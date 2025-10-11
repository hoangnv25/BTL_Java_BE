package com.BTL_JAVA.BTL.Service.Product;

import com.BTL_JAVA.BTL.DTO.Request.ApiResponse;
import com.BTL_JAVA.BTL.DTO.Request.SalesCreationRequest;
import com.BTL_JAVA.BTL.DTO.Request.SalesUpdateRequest;
import com.BTL_JAVA.BTL.DTO.Response.SalesResponse;
import com.BTL_JAVA.BTL.Entity.Product.Sales;
import com.BTL_JAVA.BTL.Entity.Product.Product;
import com.BTL_JAVA.BTL.Repository.SalesRepository;
import com.BTL_JAVA.BTL.Repository.ProductRepository;
import com.BTL_JAVA.BTL.Exception.AppException;
import com.BTL_JAVA.BTL.Exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SalesService {

    SalesRepository salesRepository;
    ProductRepository productRepository;

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
            validateSaleValue(request.getValue());

            boolean isActive = calculateActiveStatus(request.getStDate(), request.getEndDate());

            Sales sale = Sales.builder()
                    .name(request.getName())
                    .value(request.getValue())
                    .stDate(request.getStDate())
                    .endDate(request.getEndDate())
                    .active(isActive)
                    .build();

            Sales saved = salesRepository.save(sale);

            if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
                addProductsToSale(saved.getId(), request.getProductIds());
            }

            return ApiResponse.<SalesResponse>builder()
                    .code(1000).message("Success")
                    .result(toResponse(saved, isActive))
                    .build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_FAILED);
        }
    }

    @Transactional
    public ApiResponse<SalesResponse> update(SalesUpdateRequest request) {
        try {
            Sales sale = salesRepository.findById(request.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.SALE_NOT_EXISTED));

            if (request.getName() != null) sale.setName(request.getName());
            if (request.getValue() != null) {
                validateSaleValue(request.getValue());
                sale.setValue(request.getValue());
            }
            if (request.getStDate() != null) sale.setStDate(request.getStDate());
            if (request.getEndDate() != null) sale.setEndDate(request.getEndDate());

            if (request.getStDate() != null && request.getEndDate() != null) {
                validateSaleDates(request.getStDate(), request.getEndDate());
            }

            boolean isActive = calculateActiveStatus(
                    request.getStDate() != null ? request.getStDate() : sale.getStDate(),
                    request.getEndDate() != null ? request.getEndDate() : sale.getEndDate()
            );
            sale.setActive(isActive);

            Sales saved = salesRepository.save(sale);

            if (request.getAddProductIds() != null && !request.getAddProductIds().isEmpty()) {
                addProductsToSale(saved.getId(), request.getAddProductIds());
            }

            if (request.getRemoveProductIds() != null && !request.getRemoveProductIds().isEmpty()) {
                removeProductsFromSale(saved.getId(), request.getRemoveProductIds());
            }

            return ApiResponse.<SalesResponse>builder()
                    .code(1000).message("Success")
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

            if (!sale.getProducts().isEmpty()) {
                sale.getProducts().clear();
                salesRepository.save(sale);
            }

            salesRepository.delete(sale);

            return ApiResponse.<Void>builder()
                    .code(1000).message("Deleted")
                    .result(null)
                    .build();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        }
    }

    private void addProductsToSale(Integer saleId, Set<Integer> productIds) {
        Sales sale = salesRepository.findById(saleId)
                .orElseThrow(() -> new AppException(ErrorCode.SALE_NOT_EXISTED));

        var productsToAdd = productRepository.findAllById(productIds);
        var foundIds = productsToAdd.stream()
                .map(Product::getProductId)
                .collect(Collectors.toSet());

        var missingIds = productIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        var existingProductIds = sale.getProducts().stream()
                .map(Product::getProductId)
                .collect(Collectors.toSet());

        var newProducts = productsToAdd.stream()
                .filter(product -> !existingProductIds.contains(product.getProductId()))
                .collect(Collectors.toSet());

        sale.getProducts().addAll(newProducts);
        salesRepository.save(sale);
    }

    private void removeProductsFromSale(Integer saleId, Set<Integer> productIds) {
        Sales sale = salesRepository.findById(saleId)
                .orElseThrow(() -> new AppException(ErrorCode.SALE_NOT_EXISTED));

        boolean removed = sale.getProducts().removeIf(product ->
                productIds.contains(product.getProductId()));

        if (!removed) {
            throw new AppException(ErrorCode.PRODUCT_NOT_IN_SALE);
        }

        salesRepository.save(sale);
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

    private void validateSaleValue(java.math.BigDecimal value) {
        if (value.compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new AppException(ErrorCode.INVALID_SALE_VALUE);
        }
    }

    private SalesResponse toResponse(Sales sales, boolean isActive) {
        Set<Integer> productIds = (sales.getProducts() == null) ? Set.of()
                : sales.getProducts().stream()
                .map(Product::getProductId)
                .collect(Collectors.toSet());

        return SalesResponse.builder()
                .id(sales.getId())
                .name(sales.getName())
                .value(sales.getValue())
                .stDate(sales.getStDate())
                .endDate(sales.getEndDate())
                .active(isActive)
                .productIds(productIds)
                .build();
    }
}