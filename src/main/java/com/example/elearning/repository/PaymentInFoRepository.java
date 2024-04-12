package com.example.elearning.repository;

import com.example.elearning.dto.PaymentInfoDTO;
import com.example.elearning.dto.UserCourseDto;
import com.example.elearning.model.PaymentInFo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PaymentInFoRepository extends JpaRepository<PaymentInFo, Long> {

    @Query("select new com.example.elearning.dto.PaymentInfoDTO(e) from PaymentInFo e " +
            " where e.vnp_SecureHash = :vnp_SecureHash ")
    List<PaymentInfoDTO> getPaymentInfoByVnp_SecureHash(String vnp_SecureHash);
}
