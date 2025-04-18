package cn.cuit.domain;

import lombok.Data;

@Data
public class LoginRequest {
    private String account;
    private String password;
    private String type;
} 