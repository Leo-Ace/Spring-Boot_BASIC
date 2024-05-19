package com.demo02.identityservice.service;

import com.demo02.identityservice.dto.request.UserCreationRequest;
import com.demo02.identityservice.dto.request.UserUpdateRequest;
import com.demo02.identityservice.dto.response.UserResponse;
import com.demo02.identityservice.entity.User;
import com.demo02.identityservice.enums.Role;
import com.demo02.identityservice.exception.AppException;
import com.demo02.identityservice.exception.ErrorCode;
import com.demo02.identityservice.mapper.UserMapper;
import com.demo02.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest request) {
        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')") // kiem tra truoc khi chay phuong thuc
    public List<UserResponse> getUsers() {
        List<UserResponse> userResponses = new ArrayList<UserResponse>();
        userResponses = userRepository.findAll()
                .stream().map(userMapper::toUserResponse).toList();
        return userResponses;
    }

    // @PostAuthorize("hasRole('ADMIN')") // kiem tra sau khi chay phuong thuc
    // neu gia tri tra ve cua phuong thuc thoa man dieu kien thi thuc hien
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String deleteUser(String userId) {
        userRepository.deleteById(userId);
        return "User has been deleted!";
    }
}
