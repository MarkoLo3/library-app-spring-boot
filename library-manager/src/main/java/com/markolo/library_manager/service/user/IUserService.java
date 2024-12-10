package com.markolo.library_manager.service.user;

import com.markolo.library_manager.model.User;
import com.markolo.library_manager.request.CreateUserRequest;

import java.util.List;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(CreateUserRequest request, Long userId);
    List<User> getAllUsers();
    void deleteUser(Long userId);



}
