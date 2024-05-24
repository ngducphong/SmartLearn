package com.example.elearning.dto;

import com.example.elearning.dto.base.BaseObjectDto;
import com.example.elearning.model.PaymentInFo;
import com.example.elearning.model.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfoDTO extends BaseObjectDto {
    private String vnp_Amount;
    private String vnp_BankCode;
    private String vnp_BankTranNo;
    private String vnp_CardType;
    private String vnp_OrderInfo;
    private String vnp_PayDate;
    private String vnp_ResponseCode;
    private String vnp_TmnCode;
    private String vnp_TransactionNo;
    private String vnp_TransactionStatus;
    private String vnp_TxnRef;
    private String vnp_SecureHash;
    private UsersDto usersDto;

    public PaymentInfoDTO() {
    }

    public PaymentInfoDTO(PaymentInFo entity) {
        this.id = entity.getId();
        this.vnp_Amount = entity.getVnp_Amount();
        this.vnp_BankCode = entity.getVnp_BankCode();
        this.vnp_BankTranNo = entity.getVnp_BankTranNo();
        this.vnp_CardType = entity.getVnp_CardType();
        this.vnp_OrderInfo = entity.getVnp_OrderInfo();
        this.vnp_PayDate = entity.getVnp_PayDate();
        this.vnp_ResponseCode = entity.getVnp_ResponseCode();
        this.vnp_TmnCode = entity.getVnp_TmnCode();
        this.vnp_TransactionNo = entity.getVnp_TransactionNo();
        this.vnp_TransactionStatus = entity.getVnp_TransactionStatus();
        this.vnp_TxnRef = entity.getVnp_TxnRef();
        this.vnp_SecureHash = entity.getVnp_SecureHash();
        if (entity.getUsers() != null) {
            this.usersDto = new UsersDto(entity.getUsers());
        }
    }

    public PaymentInfoDTO(String vnp_Amount, String vnp_BankCode, String vnp_BankTranNo, String vnp_CardType
            , String vnp_OrderInfo, String vnp_PayDate, String vnp_ResponseCode, String vnp_TmnCode
            , String vnp_TransactionNo, String vnp_TransactionStatus, String vnp_TxnRef, String vnp_SecureHash) {
        this.vnp_Amount = vnp_Amount;
        this.vnp_BankCode = vnp_BankCode;
        this.vnp_BankTranNo = vnp_BankTranNo;
        this.vnp_CardType = vnp_CardType;
        this.vnp_OrderInfo = vnp_OrderInfo;
        this.vnp_PayDate = vnp_PayDate;
        this.vnp_ResponseCode = vnp_ResponseCode;
        this.vnp_TmnCode = vnp_TmnCode;
        this.vnp_TransactionNo = vnp_TransactionNo;
        this.vnp_TransactionStatus = vnp_TransactionStatus;
        this.vnp_TxnRef = vnp_TxnRef;
        this.vnp_SecureHash = vnp_SecureHash;
    }

    public PaymentInfoDTO(PaymentInFo entity, Boolean isGetFull) {
        this.id = entity.getId();
        this.vnp_Amount = entity.getVnp_Amount();
        this.vnp_BankCode = entity.getVnp_BankCode();
        this.vnp_BankTranNo = entity.getVnp_BankTranNo();
        this.vnp_CardType = entity.getVnp_CardType();
        this.vnp_OrderInfo = entity.getVnp_OrderInfo();
        this.vnp_PayDate = entity.getVnp_PayDate();
        this.vnp_ResponseCode = entity.getVnp_ResponseCode();
        this.vnp_TmnCode = entity.getVnp_TmnCode();
        this.vnp_TransactionNo = entity.getVnp_TransactionNo();
        this.vnp_TransactionStatus = entity.getVnp_TransactionStatus();
        this.vnp_TxnRef = entity.getVnp_TxnRef();
        this.vnp_SecureHash = entity.getVnp_SecureHash();
        if (entity.getUsers() != null) {
            this.usersDto = new UsersDto(entity.getUsers());
        }
        if (isGetFull) {
            this.createDate = entity.getCreateDate();
            this.modifyBy = entity.getModifyBy();
            this.createBy = entity.getCreateBy();
            this.modifyDate = entity.getModifyDate();
        }


    }
}
