package com.example.elearning.security.user_principal;

import com.example.elearning.model.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserPrincipal implements UserDetails {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String fullName;
    private boolean voided;

    public UserPrincipal(Users user) {
        this.id = user.getId();
        this.fullName =user.getFullName();
        this.username= user.getUsername();
        this.phone= user.getPhone();
        this.email= user.getEmail();
        this.password =user.getPassword();
        if(user.getVoided() == null){
            this.voided = false;
        }
        else {
            this.voided = user.getVoided();
        }
        this.authorities = user.getRoles().stream().map(item->new SimpleGrantedAuthority(item.getRoleName().toString())).toList();}
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
        return !voided;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !voided;
    }

}
