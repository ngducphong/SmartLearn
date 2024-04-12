package com.example.elearning.security.user_principal;

import com.example.elearning.model.Users;
import com.example.elearning.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginInput) throws UsernameNotFoundException {
        Users users = userRepository.findUsersByUsername(loginInput ).orElseThrow(() -> new RuntimeException("username not found"));
        return new UserPrincipal(users);
    }
}
