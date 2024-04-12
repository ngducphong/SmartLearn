package com.example.elearning.interceptor;

import com.example.elearning.model.Users;
import com.example.elearning.repository.UserRepository;
import com.example.elearning.security.jwt.JwtProvider;
import com.example.elearning.security.user_principal.UserDetailService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    UserDetailService userDetailService;
    @Autowired
    public AuthenticationInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }
    private final String SECRET_KEY = "abxbxx";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Lấy token từ header hoặc các nơi khác trong request
        String token = this.getTokenFromRequest(request);
        if(token == null || token.isEmpty())
            return true;

        String userName = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();

        UserDetails userDetails = userDetailService.loadUserByUsername(userName);
        if (!userDetails.isEnabled()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Account is locked");
            return false;
        }
        // Nếu token và tài khoản đều hợp lệ, tiếp tục xử lý request
        return true;
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
