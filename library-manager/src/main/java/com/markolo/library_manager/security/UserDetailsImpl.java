package com.markolo.library_manager.security;

import com.markolo.library_manager.model.Role;
import com.markolo.library_manager.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {


    private Long id;

    private String email;

    private String username;

    private String password;

    private String name;

    private Role role;


    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsImpl(Long id, String email, String username, String password, String name, Collection<? extends GrantedAuthority> authorities) {
        this.id=id;
        this.email=email;
        this.username=username;
        this.password=password;
        this.name=name;
        this.authorities=authorities; //
    }


    public static UserDetailsImpl build(User user) {


        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                List.of(authority) //
        );
    }




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
