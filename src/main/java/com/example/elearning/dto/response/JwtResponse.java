package com.example.elearning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
    private String accessToken;

    private String refreshToken;

    private Long expired;

    private final String type = "Bearer";

    private String fullName;

    private String username;

    private Set<String> roles;
}
