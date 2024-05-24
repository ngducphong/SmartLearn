package com.example.elearning.service.impl;

import com.example.elearning.config.ConfigVNPay;
import com.example.elearning.dto.*;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Chapter;
import com.example.elearning.model.PaymentInFo;
import com.example.elearning.model.Users;
import com.example.elearning.repository.PaymentInFoRepository;
import com.example.elearning.repository.UserCourseRepository;
import com.example.elearning.repository.UserRepository;
import com.example.elearning.security.jwt.JwtProvider;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.PaymentService;
import com.example.elearning.service.UserCourseService;
import com.example.elearning.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    UserCourseService userCourseService;
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    UserCourseRepository userCourseRepository;
    @Autowired
    PaymentInFoRepository paymentInFoRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> createPayment(HttpServletRequest request, UserCourseDto dto) throws Exception {
        if (dto == null || dto.getCourseDto() == null || dto.getCourseDto().getId() == null)
            throw new CustomException("UserCourseDto is not null");

        CourseDto courseDto = courseService.getCourseDtoById(dto.getCourseDto().getId());
        Users users = userService.getCurrentUser();

        if (userCourseRepository.getByUsersIdAndCourseId(users.getId(), courseDto.getId()).size() > 0) {
            throw new CustomException("UserCourse exist");
        }

        if (courseDto.getPrice() == null || courseDto.getPrice() == 0) {
            UserCourseDto userCourseDto = new UserCourseDto();
            userCourseDto.setCourseDto(courseDto);
            userCourseDto = userCourseService.saveUserCourse(userCourseDto);
            return ResponseEntity.status(HttpStatus.OK).body(userCourseDto);
        }

        long amount = courseDto.getPrice().longValue() * 100;


        String vnp_TxnRef = ConfigVNPay.getRandomNumber(8);
        String vnp_IpAddr = ConfigVNPay.getIpAddress(request);
//        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = ConfigVNPay.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", ConfigVNPay.vnp_Version);
        vnp_Params.put("vnp_Command", ConfigVNPay.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", this.encrypt(this.getTokenFromRequest(request) + "/" + courseDto.getId(), ConfigVNPay.secretKey));
        vnp_Params.put("vnp_OrderType", ConfigVNPay.orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", ConfigVNPay.vnp_ReturnUrl);
//        vnp_Params.put("vnp_ReturnUrl", ConfigVNPay.vnp_ReturnUrl + "/" + courseDto.getId());

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = ConfigVNPay.hmacSHA512(ConfigVNPay.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = ConfigVNPay.vnp_PayUrl + "?" + queryUrl;

        PaymentResDto paymentResDto = new PaymentResDto();
        paymentResDto.setStatus("Ok");
        paymentResDto.setMessage("Successfully");
        paymentResDto.setUrl(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResDto);
    }

    @Override
    public PaymentInfoDTO savePaymentInfo(PaymentInfoDTO paymentInfoDTO, Users users) {
        PaymentInFo paymentInFo = new PaymentInFo();
        paymentInFo.setVnp_Amount(paymentInfoDTO.getVnp_Amount());
        paymentInFo.setVnp_BankCode(paymentInfoDTO.getVnp_BankCode());
        paymentInFo.setVnp_BankTranNo(paymentInfoDTO.getVnp_BankTranNo());
        paymentInFo.setVnp_CardType(paymentInfoDTO.getVnp_CardType());
        paymentInFo.setVnp_OrderInfo(users.getUsername());
        paymentInFo.setVnp_PayDate(paymentInfoDTO.getVnp_PayDate());
        paymentInFo.setVnp_ResponseCode(paymentInfoDTO.getVnp_ResponseCode());
        paymentInFo.setVnp_TmnCode(paymentInfoDTO.getVnp_TmnCode());
        paymentInFo.setVnp_TransactionNo(paymentInfoDTO.getVnp_TransactionNo());
        paymentInFo.setVnp_TransactionStatus(paymentInfoDTO.getVnp_TransactionStatus());
        paymentInFo.setVnp_TxnRef(paymentInfoDTO.getVnp_TxnRef());
        paymentInFo.setVnp_SecureHash(paymentInfoDTO.getVnp_SecureHash());

        paymentInFo.setUsers(users);
        paymentInFo = paymentInFoRepository.save(paymentInFo);
        return new PaymentInfoDTO(paymentInFo);
    }

    @Override
    public void transaction(Long courseId, String username, String vnp_SecureHash) throws CustomException {
        if (!paymentInFoRepository.getPaymentInfoByVnp_SecureHash(vnp_SecureHash).isEmpty()) {
            throw new CustomException("vnp_SecureHash exists");
        }

        userCourseService.saveUserCourseByUsernameAndCourseId(courseId, username);
    }

    @Override
    public int orderReturn(HttpServletRequest request) throws Exception {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = ConfigVNPay.hashAllFields(fields);
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {

                String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
                if (vnp_OrderInfo == null || vnp_OrderInfo.isEmpty())
                    throw new CustomException("course Id OR username is null");

                String tokenAndCourseId = this.decrypt(vnp_OrderInfo, ConfigVNPay.secretKey);
                String[] parts = tokenAndCourseId.split("/");
                String token = parts[0];
                String courseId = parts[1];

                String username = jwtProvider.getUsernameFromToken(token);

                this.transaction(Long.valueOf(courseId), username, vnp_SecureHash);

                String amount = request.getParameter("vnp_Amount");
                String bankCode = request.getParameter("vnp_BankCode");
                String bankTranNo = request.getParameter("vnp_BankTranNo");
                String cardType = request.getParameter("vnp_CardType");
                String orderInfo = request.getParameter("vnp_OrderInfo");
                String payDate = request.getParameter("vnp_PayDate");
                String responseCode = request.getParameter("vnp_ResponseCode");
                String tmnCode = request.getParameter("vnp_TmnCode");
                String transactionNo = request.getParameter("vnp_TransactionNo");
                String transactionStatus = request.getParameter("vnp_TransactionStatus");
                String txnRef = request.getParameter("vnp_TxnRef");
                String secureHash = request.getParameter("vnp_SecureHash");

                Users users = userRepository.findUsersByUsername(username).orElseThrow(() -> new CustomException("UserCourse not found"));

                PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO(amount, bankCode, bankTranNo, cardType, orderInfo,
                        payDate, responseCode, tmnCode, transactionNo, transactionStatus, txnRef, secureHash);

                this.savePaymentInfo(paymentInfoDTO, users);

                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public static String encrypt(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}
