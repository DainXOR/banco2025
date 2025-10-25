package com.udea.dainxor.banco2025.types;

import jakarta.validation.constraints.NotNull;

public record ResponseBody<DTO>(DTO data, @NotNull String message) {
    ResponseBody(DTO data) {
        this(data, "");
    }
    public ResponseBody(String message) {
        this(null, message);
    }

    public static <DTO> ResponseBody<DTO> of(DTO data) {
        return new ResponseBody<>(data);
    }
    public static <DTO> ResponseBody<DTO> of(String message) {
        return new ResponseBody<>(message);
    }
}
