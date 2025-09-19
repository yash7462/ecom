package com.ecom.enums;

public enum ERole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER"),
    ROLE_CUSTOMER("ROLE_CUSTOMER");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Optional: find Role by string value
    public static ERole fromValue(String value) {
        for (ERole role : ERole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
