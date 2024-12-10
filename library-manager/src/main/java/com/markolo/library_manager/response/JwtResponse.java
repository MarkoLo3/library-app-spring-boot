package com.markolo.library_manager.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
public class JwtResponse {

    private String token;

    private String type = "Bearer";

    private Long id;

    private String username;

    private String email;

    private String name;

    private String role; // POPRAVITI ROLE

    public JwtResponse(String accesToken, Long id, String username, String email, String name , String role) {
        this.token=accesToken;
        this.id=id;
        this.username=username;
        this.email=email;
        this.name=name;
        this.role=role;
    }
}
