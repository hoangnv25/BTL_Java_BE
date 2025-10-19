package com.BTL_JAVA.BTL.DTO.Response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductInCategoryResponse {
        Integer productId;
        String  title;
        String  description;
        Double  price;
        String  image;
        Integer categoryId;
        Integer variationCount;
//        Set<Integer> variationIds;
        List<ProductVariationResponse> variations;
}
