package com.BTL_JAVA.BTL.DTO.Request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateRequest {

    private String fullName;

    private String email;

    private String password;

    private String phoneNumber;

    private List<String> roles;

}
