package com.BTL_JAVA.BTL.mapper;

import com.BTL_JAVA.BTL.DTO.Request.UserCreationRequest;
import com.BTL_JAVA.BTL.Entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T23:39:11+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        return user;
    }
}
