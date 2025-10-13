package com.BTL_JAVA.BTL.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SalesUpdateRequest {
    String name;
    BigDecimal value;
    LocalDateTime stDate;
    LocalDateTime endDate;
    Boolean active;
    Set<Integer> addProductIds;
    Set<Integer> removeProductIds;
}