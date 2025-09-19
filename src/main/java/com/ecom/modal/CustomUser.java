package com.ecom.modal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String mobile;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean accountNonExpired = true;
    private Boolean accountNonLocked = true;
    private Boolean credentialsNonExpired = true;
    private Boolean enabled = true;

    public CustomUser(Long userId, String email, String password, String mobile, List<GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.mobile=mobile;
        this.authorities = authorities;

    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    public Boolean getAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.getAccountNonExpired();
    }

    @JsonIgnore
    public Boolean getAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getAccountNonLocked();
    }

    @JsonIgnore
    public Boolean getCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.getCredentialsNonExpired();
    }

    @JsonIgnore
    public Boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.getEnabled();
    }


    public static CustomUser build(UserModal user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().getValue()))
                .collect(Collectors.toList());

        return new CustomUser(
                user.getUserId(),
                user.getEmail(),
                user.getPassword(),
                user.getMobile(),
                authorities);
    }

//    public static CustomUser build(UserModal user, List<Permission> permissions) {
//        List<GrantedAuthority> authorities = permissions.stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
//
//        return new CustomUser(
//                user.getUserId(),
//                user.getEmail(),
//                user.getPassword(),
//                user.getMobile(),
//                authorities);
//    }

}
