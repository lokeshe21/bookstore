package com.bookstore.applicaton.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int statusCode; // Added for HTTP status code
    private boolean success;
    private String message;
    private T data;


}