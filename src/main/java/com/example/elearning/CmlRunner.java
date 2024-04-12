package com.example.elearning;


import com.example.elearning.constant.RoleName;
import com.example.elearning.dto.request.UserInfoRequest;
import com.example.elearning.model.Roles;
import com.example.elearning.repository.RoleRepository;
import com.example.elearning.repository.UserRepository;
import com.example.elearning.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class CmlRunner implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        this.createRole();
        this.createUser();
    }
    private void createRole(){
        // ROLE_ADMIN,ROLE_SUBADMIN,ROLE_USER
        if(roleRepository.countRole() == 0){
            List<Roles> roles = new ArrayList<>();
            Roles roleAdmin = new Roles();
            Roles roleSubAdmin = new Roles();
            Roles roleUser = new Roles();

            roleAdmin.setRoleName(RoleName.ROLE_ADMIN);
            roleSubAdmin.setRoleName(RoleName.ROLE_SUBADMIN);
            roleUser.setRoleName(RoleName.ROLE_USER);

            roles.add(roleAdmin);
            roles.add(roleSubAdmin);
            roles.add(roleUser);
            roles = roleRepository.saveAll(roles);
            System.out.println(" ================== Create Role ================== ");
        }
    }
    private void createUser() throws Exception {
        if(userRepository.countUser() == 0){
            UserInfoRequest userInfoRequest = new UserInfoRequest();
            userInfoRequest.setFullName("ADMIN");
            userInfoRequest.setPhone("0353712221");
            userInfoRequest.setEmail("ng.duc.phong010402@gmail.com");
            userInfoRequest.setPassword("123456");
            userInfoRequest.setUsername("admin");

            Set<RoleName> roleNames = new HashSet<>();
            roleNames.add(RoleName.ROLE_ADMIN);
            userInfoRequest.setRole(roleNames);
            userService.createUser(userInfoRequest);

            System.out.println(" ================== Create Admin ================== ");
        }
    }
}
