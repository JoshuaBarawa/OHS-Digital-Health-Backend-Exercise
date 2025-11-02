package org.example.patient.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private int status;
    private T data;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, 200, data);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(message, 201, data);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(message, 404, null);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(message, 400, null);
    }
}