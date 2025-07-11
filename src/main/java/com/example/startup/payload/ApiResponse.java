package com.example.startup.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Object errors;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ok("Success", data);
    }

    public static ApiResponse<?> ok(String message) {
        return ok(message, null);
    }

    public static ApiResponse<?> ok() {
        return ok("Success", null);
    }

    public static ApiResponse<?> error(String message) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

//    public static ApiResponse<?> error(String message, Object errors) {
//        return ApiResponse.builder()
//                .success(false)
//                .message(message)
//                .errors(errors)
//                .timestamp(LocalDateTime.now())
//                .build();
//    }

     public static <T> ApiResponse<T> error(String message, T data) {
         return ApiResponse.<T>builder()
                 .success(false)
                 .message(message)
                 .data(data)
                 .timestamp(LocalDateTime.now())
                 .build();
     }
}