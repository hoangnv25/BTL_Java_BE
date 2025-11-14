package com.BTL_JAVA.BTL.Repository.httpclient;

import com.BTL_JAVA.BTL.DTO.Response.Auth.FacebookOutboundUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "outbound-facebook-user-client", url = "https://graph.facebook.com")
public interface OutboundFacebookUserClient {
    @GetMapping(value = "/me")
    FacebookOutboundUserResponse getUserInfo(
            @RequestParam("fields") String fields,
            @RequestParam("access_token") String accessToken
    );
}

