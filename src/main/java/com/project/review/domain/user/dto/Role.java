package com.project.review.domain.user.dto;

public enum Role {
    ADMIN(9, "ROLE_ADMIN"),
    USER(1, "ROLE_USER");

    private final int code;
    private final String roleName;

    Role(int code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public int getCode() {
        return code;
    }

    public String getRoleName() {
        return roleName;
    }

    // 숫자로 역할 찾기
    public static String getRoleByCode(int code) {
        for (Role role : Role.values()) {
            if (role.getCode() == code) {
                return role.getRoleName();
            }
        }
        throw new IllegalArgumentException("Invalid role code: " + code);
    }
}