package com.example.elearning.repository;

import com.example.elearning.constant.RoleName;
import com.example.elearning.model.Course;
import com.example.elearning.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findUsersByPhone(String phone);
    Optional<Users> findUsersByUsername(String username);
    Optional<Users> findUsersByEmail(String email);

    @Query("select e from Users e" +
            " where  ( e.username = :loginInput or e.email = :loginInput or e.phone = :loginInput)")
    Optional<Users> findUsersByPhoneOrEmailOrUsername(String loginInput);

    boolean existsByPhone(String phone);
    boolean existsByUsername(String phone);
    boolean existsByEmail(String phone);

    @Query("select count(u.id) from Users u")
    Long countUser();


    @Query("select  u from Users u " +
                "WHERE (LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%')) OR :name = '' OR :name is null ) " +
            "AND (u.phone = :phone OR :phone = '' or :phone is null) " +
            "AND (u.username = :username OR :username = '' or :username is null) " +
            "AND (u.email = :email OR :email = '' or :email is null) " +
            "AND (u.voided = :voided  or :voided is null) " +
            "AND (DATE(u.createDate) = DATE(:createDate) OR  :createDate is null) " +
            "")
    Page<Users> findUsersByFullNameAndPhone(String name, String username, String email, String phone, Pageable pageable, Date createDate, Boolean voided);

//    @Query("select u.favourite from Users u where u.id = :userId")
//    Page<Course> getWistListByUserId(Long userId, Pageable pageable);

        @Query(value = "select r.role_name from user_role u, roles r WHERE r.id = u.role_id AND (u.user_id = :userId) ", nativeQuery = true)
        List<Byte> getNameRolesByUserId(Long userId);

}
