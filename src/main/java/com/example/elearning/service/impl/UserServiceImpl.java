package com.example.elearning.service.impl;

import com.example.elearning.constant.RoleName;
import com.example.elearning.dto.request.*;
import com.example.elearning.dto.response.JwtResponse;
import com.example.elearning.dto.response.UserResponse;
import com.example.elearning.exception.CustomException;
import com.example.elearning.model.Roles;
import com.example.elearning.model.Users;
import com.example.elearning.repository.UserRepository;
import com.example.elearning.security.jwt.JwtProvider;
import com.example.elearning.security.user_principal.UserPrincipal;
import com.example.elearning.service.RoleService;
import com.example.elearning.service.UserService;
import com.infobip.ApiException;
import com.infobip.api.MessagesApi;
import com.infobip.model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infobip.ApiClient;
import com.infobip.ApiKey;
import com.infobip.BaseUrl;
//import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.Resource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Value("604800016")
    private Long EXPIRED;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;
    @Resource
    private JavaMailSender mailSender;

    @PersistenceContext
    private EntityManager entityManager;


    @Value("${infobip.apiKey}")
    private String apiKey;

    @Value("${infobip.baseUrl}")
    private String baseUrl;


    @Override
    public void registerUser(UserInfoRequest userInfoRequest) throws CustomException {
        if (userRepository.existsByUsername(userInfoRequest.getUsername())) {
            throw new CustomException("Username is register");
        }

        setInfoUser(userInfoRequest, Set.of(RoleName.ROLE_USER), new Users());
    }

    @Override
    public UserResponse createUser(UserInfoRequest request) throws CustomException {
        Users users = new Users();
        setInfoUser(request, request.getRole(), users);
        return new UserResponse(users);
    }

    @Override
    public void registerSubAdmin(UserInfoRequest request) throws CustomException {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException("Username is register");
        }
        setInfoUser(request, Set.of(RoleName.ROLE_SUBADMIN), new Users());
    }

    @Override
    public JwtResponse login(UserLogin userLogin) throws CustomException {
        Authentication authentication;
        try {
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        } catch (AuthenticationException e) {
            throw new CustomException("Bad credentials:" + e.getMessage());
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        //        Users users = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new CustomException("user not found"));
//        Users users = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new CustomException("user not found"));
//        String refreshToken = null;
//        if (users.getRefreshToken() == null || users.getRefreshToken().isEmpty()) {
//            refreshToken = jwtProvider.generateRefreshToken(userPrincipal);
//
//        } else {
//            if (!jwtProvider.isTokenExpired(users.getRefreshToken())) {
//                refreshToken = users.getRefreshToken();
//            } else {
//                refreshToken = jwtProvider.generateRefreshToken(userPrincipal);
//            }
//        }
//        // lưu refresh token vào database
//        users.setRefreshToken(refreshToken);


        // thực hiện trả về cho người dùng
        return JwtResponse.builder()
                .accessToken(jwtProvider.generateToken(userPrincipal))
                .refreshToken(null)
                .expired(EXPIRED)
                .fullName(userPrincipal.getFullName())
                .username(userPrincipal.getUsername())
                .roles(userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public String handleLogout(Authentication authentication) {
        return null;
    }

    @Override
    public void editInfoUser(EditUserRequest editUserRequest) throws CustomException {
        Users users = this.getCurrentUser();
        if (editUserRequest.getUsername() != null && !editUserRequest.getUsername().isEmpty() &&
                !Objects.equals(users.getUsername(), editUserRequest.getUsername()) &&
                userRepository.existsByUsername(editUserRequest.getUsername())) {
            throw new CustomException("Username is register");
        }
        if (editUserRequest.getPhone() != null && !editUserRequest.getPhone().isEmpty() &&
                !Objects.equals(users.getPhone(), editUserRequest.getPhone()) &&
                userRepository.existsByPhone(editUserRequest.getPhone())) {
            throw new CustomException("Phone is register");
        }
        if (editUserRequest.getEmail() != null && !editUserRequest.getEmail().isEmpty() &&
                !Objects.equals(users.getEmail(), editUserRequest.getEmail()) &&
                userRepository.existsByEmail(editUserRequest.getEmail())) {
            throw new CustomException("Email is register");
        }

        copyPropertiesUser(editUserRequest, users);

        if (editUserRequest.getPassword() != null && !editUserRequest.getPassword().isEmpty()) {
            users.setPassword(passwordEncoder.encode(editUserRequest.getPassword()));
        }

        userRepository.save(users);
    }

    @Override
    public void changePassword(ChangePasswordRequest passwordRequest) throws CustomException {
        Users users = this.getCurrentUser();
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), users.getPassword())) {
            throw new CustomException("Wrong password");
        }
        users.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(users);
    }

    @Override
    public void editUser(EditUserRequest editUserRequest, Long id) throws CustomException {
        Users users = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"));
        if (!users.getUsername().equals(editUserRequest.getUsername())) {
            if (userRepository.existsByUsername(editUserRequest.getUsername())) {
                throw new CustomException("Username is register");
            }
        }
        if (editUserRequest.getPhone() != null && !editUserRequest.getPhone().isEmpty() &&
                !Objects.equals(users.getPhone(), editUserRequest.getPhone()) &&
                userRepository.existsByPhone(editUserRequest.getPhone())) {
            throw new CustomException("Phone is register");
        }
        if (editUserRequest.getEmail() != null && !editUserRequest.getEmail().isEmpty() &&
                !Objects.equals(users.getEmail(), editUserRequest.getEmail()) &&
                userRepository.existsByEmail(editUserRequest.getEmail())) {
            throw new CustomException("Email is register");
        }


        copyPropertiesUser(editUserRequest, users);
        if (editUserRequest.getPassword() != null && !editUserRequest.getPassword().isEmpty()) {
            users.setPassword(passwordEncoder.encode(editUserRequest.getPassword()));
        }
        if (editUserRequest.getRole() != null && !editUserRequest.getRole().isEmpty()) {
            Set<Roles> roles = new HashSet<>();
            editUserRequest.getRole().forEach(e -> {
                switch (e) {
                    case "ROLE_ADMIN":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_ADMIN));
                    case "ROLE_SUBADMIN":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_SUBADMIN));
                    case "ROLE_USER":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_USER));
                }
            });
            users.setRoles(roles);
        }
        userRepository.save(users);
    }

    @Override
    public Page<UserResponse> findAll(String name, String username, String email, String phone, Pageable pageable, String createDate, Boolean voided, String role) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Page<Users> users = userRepository.findUsersByFullNameAndPhone(name, username, email, phone, pageable,
                Objects.equals(createDate, "") || createDate == null ? null : formatter.parse(createDate), voided);
        return users.map(UserResponse::new);
    }

    @Override
    public Users getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
        return userRepository.findUsersByUsername(userPrincipal.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public boolean changeStatusActiveUser(Long id) throws CustomException {
        Users users = userRepository.findById(id).orElseThrow(() -> new CustomException("User not found"));
        if (users.getVoided() == null) {
            users.setVoided(true);
        } else {
            users.setVoided(!users.getVoided());
        }
        users = userRepository.save(users);
        return users.getVoided();
    }


    @Override
    public void resetPassword(ResetPasswordRequest request) throws CustomException, IOException, ApiException {
        MessagesApi messagesApi = null;
        MessagesApiRequest messagesApiRequest = null;
        if (request != null && request.getEmail() != null && !request.getEmail().isEmpty()) {
            Users users = userRepository.findUsersByEmail(request.getEmail()).orElseThrow(() -> new CustomException("User not found"));
            String newPassword = this.generatePassword();
            users.setPassword(passwordEncoder.encode(newPassword));
            users = userRepository.save(users);
            this.senMailResetPassword(newPassword, users);

            System.out.println("Send reset password by email");
            return;
        } else if (request != null && request.getPhone() != null && !request.getPhone().isEmpty()) {

            String BASE_URL = baseUrl;
            String ACCESS_TOKEN = apiKey;

            Users users = userRepository.findUsersByPhone(request.getPhone()).orElseThrow(() -> new CustomException("User not found"));
            String password = this.generatePassword();
            users.setPassword(passwordEncoder.encode(password));
            users = userRepository.save(users);

            String messageText = "Dear " + users.getUsername() + " , Your Password is " + password + " . Thank You.";

            String receiverPhoneNumber = "84" + users.getPhone().substring(1);

            ApiClient apiClient = ApiClient.forApiKey(ApiKey.from(ACCESS_TOKEN))
                    .withBaseUrl(BaseUrl.from(BASE_URL))
                    .build();
            messagesApi = new MessagesApi(apiClient);

            MessagesApiMessage messagesApiMessage = new MessagesApiMessage()
                    .channel(MessagesApiOutboundMessageChannel.SMS)
                    .sender(receiverPhoneNumber)
                    .destinations(
                            List.of(
                                    new MessagesApiToDestination()
                                            .to(receiverPhoneNumber)
                            )
                    )
                    .content(
                            new MessagesApiMessageContent()
                                    .body(
                                            new MessagesApiMessageTextBody()
                                                    .text(messageText)
                                    )
                    );

            messagesApiRequest = new MessagesApiRequest()
                    .addMessagesItem(messagesApiMessage);
        }
        MessagesApiResponse messageInfo = null;

        messageInfo = messagesApi
                .sendMessagesApiMessage(messagesApiRequest)
                .execute();
        System.out.println("Send reset password by phone");
        return;
    }

    @Override
    public Map<Integer, Long> getUserAccountRegistrationData(Integer year) {
        Map<Integer, Long> mapMonthData = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            mapMonthData.put(i, 0L);
        }

        // Query to get the count of registrations per month for the given year
        List<Object[]> results = entityManager.createQuery(
                        "SELECT FUNCTION('MONTH', e.createDate), COUNT(e.id) " +
                                "FROM Users e " +
                                "WHERE FUNCTION('YEAR', e.createDate) = :year " +
                                "GROUP BY FUNCTION('MONTH', e.createDate)", Object[].class)
                .setParameter("year", year)
                .getResultList();

        // Update the map with actual data from the query
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Long count = (Long) result[1];
            mapMonthData.put(month, count);
        }

        return mapMonthData;
    }

    @Override
    public Map<Integer, Double> getPaymentChartData(Integer year) {
        Map<Integer, Double> mapMonthData = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            mapMonthData.put(i, 0D);
        }

        try {
            // Query to get the sum of payments per month for the given year
            List<Object[]> results = entityManager.createQuery(
                            "SELECT FUNCTION('MONTH', e.createDate), sum(e.vnp_Amount) " +
                                    "FROM PaymentInFo  e " +
                                    "WHERE FUNCTION('YEAR', e.createDate) = :year " +
                                    "GROUP BY FUNCTION('MONTH', e.createDate)", Object[].class)
                    .setParameter("year", year)
                    .getResultList();

            // Update the map with actual data from the query
            for (Object[] result : results) {
                Integer month = (Integer) result[0];
                BigDecimal amount = (BigDecimal) result[1];
                mapMonthData.put(month, amount.doubleValue());
            }
        } catch (Exception e) {
            // Handle exceptions if necessary
            e.printStackTrace();
            // You might want to throw or handle the exception appropriately
        }

        return mapMonthData;
    }


    protected void senMailResetPassword(String password, Users user) throws CustomException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("ng.duc.phong010402@gmail.com");
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Thông báo đổi mật khẩu");
        simpleMailMessage.setText("Tài khoản: " + user.getUsername().toString() + " đổi mật khẩu thành: " + password);
        try {
            mailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new CustomException("Email sending failed");
        }
    }


    private String generatePassword() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private void setInfoUser(UserInfoRequest userInfoRequest, Set<RoleName> roleNames, Users users) throws CustomException {
        if (userRepository.existsByUsername(userInfoRequest.getUsername())) {
            throw new CustomException("Username is register");
        }
        if (userInfoRequest.getPhone() != null && !userInfoRequest.getPhone().isEmpty() &&
                userRepository.existsByPhone(userInfoRequest.getPhone())) {
            throw new CustomException("Phone is register");
        }
        if (userInfoRequest.getEmail() != null && !userInfoRequest.getEmail().isEmpty() &&
                userRepository.existsByEmail(userInfoRequest.getEmail())) {
            throw new CustomException("Email is register");
        }

        copyPropertiesUser(userInfoRequest, users);
        users.setPassword(passwordEncoder.encode(userInfoRequest.getPassword()));
        if (roleNames != null && !roleNames.isEmpty()) {
            Set<Roles> roles = new HashSet<>();
            roleNames.forEach(e -> {
                Roles roles1 = roleService.findByRoleName(e);
                roles.add(roles1);
            });
            users.setRoles(roles);
        }
        userRepository.save(users);
    }

    private void copyPropertiesUser(Object o, Users users) {
        BeanUtils.copyProperties(o, users, getNullPropertyNames(o));
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
