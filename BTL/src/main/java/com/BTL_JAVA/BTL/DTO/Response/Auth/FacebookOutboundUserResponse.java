package com.BTL_JAVA.BTL.DTO.Response.Auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FacebookOutboundUserResponse {
    String id;
    String name;
    String email;
    Picture picture;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Picture {
        PictureData data;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PictureData {
        Integer height;
        Integer width;
        Boolean isSilhouette;
        String url;
    }
}

