package com.demo02.identityservice.mapper;

import com.demo02.identityservice.dto.request.UserCreationRequest;
import com.demo02.identityservice.dto.request.UserUpdateRequest;
import com.demo02.identityservice.dto.response.UserResponse;
import com.demo02.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    // map value firstName from lastName
    // @Mapping(source = "firstName", target = "lastName")
    // does not mapping lastName
    // @Mapping(target = "lastName", ignore = true)
    UserResponse toUserResponse(User user);
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
