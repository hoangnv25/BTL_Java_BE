package com.BTL_JAVA.BTL.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(1002, "User exists", HttpStatus.BAD_REQUEST),
    UNCATEGORIED_EXCEPTION(9999, "Khong xac dinh", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_INVALID(1003, "Username must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "invalid key", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not exists", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Khong co quyen truy cap", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "You age must be at leats {min}", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(1009, "Review is not exists", HttpStatus.NOT_FOUND),
    INVALID_VARIATION(1010,"Variation is invalid",HttpStatus.BAD_REQUEST),
    DUPLICATE_VARIATION(1011,"Duplicate variation",HttpStatus.BAD_REQUEST),
    VARIATION_NOT_FOUND(1012,"Variation is not exists",HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND  (1013,"Category is not exists",HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(1014,"Product is not exists",HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1013,"Product already exists",HttpStatus.BAD_REQUEST),
    VARIATION_EXISTED(1014,"Variation already exists",HttpStatus.BAD_REQUEST),
    SALE_NOT_EXISTED(2001, "Sale does not exist", HttpStatus.NOT_FOUND),
    SALES_NOT_FOUND(2002, "No sales found", HttpStatus.NOT_FOUND),
    INVALID_SALE_DATE(2003, "End date cannot be before start date", HttpStatus.BAD_REQUEST),
    INVALID_SALE_VALUE(2004, "Sale value cannot be negative", HttpStatus.BAD_REQUEST),
    INVALID_SALE_NAME(2005, "Sale name is required", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_IN_SALE(2006, "Product already exists in this sale", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_IN_SALE(2007, "Product not found in this sale", HttpStatus.BAD_REQUEST),
    ADD_TO_SALE_FAILED(2008, "Failed to add product to sale", HttpStatus.INTERNAL_SERVER_ERROR),
    REMOVE_FROM_SALE_FAILED(2009, "Failed to remove product from sale", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_NOT_EXISTED(3001, "Product does not exist", HttpStatus.NOT_FOUND),

    CREATE_FAILED(4001, "Create operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    UPDATE_FAILED(4002, "Update operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    DELETE_FAILED(4003, "Delete operation failed", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;
}