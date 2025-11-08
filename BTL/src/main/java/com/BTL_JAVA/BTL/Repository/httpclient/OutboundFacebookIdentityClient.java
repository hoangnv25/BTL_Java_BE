package com.BTL_JAVA.BTL.Repository.httpclient;

import com.BTL_JAVA.BTL.DTO.Request.Auth.ExchangeTokenRequest;
import com.BTL_JAVA.BTL.DTO.Response.Auth.ExchangeTokenReponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "outbound-facebook-identity", url = "https://graph.facebook.com")
public interface OutboundFacebookIdentityClient {
    @GetMapping(value = "/v18.0/oauth/access_token", produces = MediaType.APPLICATION_JSON_VALUE)
    ExchangeTokenReponse exchangeToken(@QueryMap ExchangeTokenRequest request);
}

