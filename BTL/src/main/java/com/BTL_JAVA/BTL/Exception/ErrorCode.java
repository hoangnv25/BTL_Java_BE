package com.BTL_JAVA.BTL.Exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;


@Getter
public enum ErrorCode {
     USER_EXISTED(1002, "User exists",HttpStatus.BAD_REQUEST),
     UNCATEGORIED_EXCEPTION(9999, "Khong xac dinh",HttpStatus.INTERNAL_SERVER_ERROR),
     USERNAME_INVALID(1003,"Username must be at least 8 characters",HttpStatus.BAD_REQUEST),
     INVALID_PASSWORD(1004,"Password must be at least {min} characters",HttpStatus.BAD_REQUEST),
     INVALID_KEY(1001,"invalid key",HttpStatus.BAD_REQUEST),
     USER_NOT_EXISTED(1005, "User not exists",HttpStatus.NOT_FOUND),
     UNAUTHENTICATED(1006, "Unauthenticated",HttpStatus.UNAUTHORIZED),
     UNAUTHORIZED(1007, "Khong co quyen truy cap",HttpStatus.FORBIDDEN),
     INVALID_DOB(1008, "You age must be at leats {min}",HttpStatus.BAD_REQUEST),
     REVIEW_NOT_FOUND(1009, "Review is not exists", HttpStatus.NOT_FOUND)
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode=statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
