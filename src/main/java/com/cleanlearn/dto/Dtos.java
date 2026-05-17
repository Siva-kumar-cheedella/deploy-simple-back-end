package com.cleanlearn.dto;
// package com.goodmorning.dto;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;
// import lombok.Data;

// // ─── Request DTOs ─────────────────────────────────────────

// @Data
// class RegisterRequest {
//     @NotBlank(message = "Name is required")
//     private String name;

//     @Email(message = "Invalid email")
//     @NotBlank(message = "Email is required")
//     private String email;

//     @NotBlank(message = "Password is required")
//     @Size(min = 6, message = "Password must be at least 6 characters")
//     private String password;
// }

// @Data
// class LoginRequest {
//     @Email
//     @NotBlank
//     private String email;

//     @NotBlank
//     private String password;
// }

// // ─── Response DTOs ────────────────────────────────────────

// @Data
// class AuthResponse {
//     private String token;
//     private String name;
//     private String email;
//     private String message;

//     public AuthResponse(String token, String name, String email) {
//         this.token = token;
//         this.name = name;
//         this.email = email;
//         this.message = "success";
//     }
// }

// @Data
// class ErrorResponse {
//     private String message;

//     public ErrorResponse(String message) {
//         this.message = message;
//     }
// }
