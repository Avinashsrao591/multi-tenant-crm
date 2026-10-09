package com.crm.crm_backend.dto;

public class LoginResponse {

    private String token;
    private Long userId;
    private Long tenantId;
    private String role;

    public LoginResponse(
            String token,
            Long userId,
            Long tenantId,
            String role) {

        this.token = token;
        this.userId = userId;
        this.tenantId = tenantId;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getRole() {
        return role;
    }
}