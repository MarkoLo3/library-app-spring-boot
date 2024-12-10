package com.markolo.library_manager.request;

import com.markolo.library_manager.model.Role;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private String name;
    private Role role;
    private String username;

}
