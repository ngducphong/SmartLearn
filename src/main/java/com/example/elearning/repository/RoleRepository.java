package com.example.elearning.repository;

import com.example.elearning.constant.RoleName;
import com.example.elearning.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(RoleName roleName);
    @Query("select count(u.id) from Roles u")
    Long countRole();
}
