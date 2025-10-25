package com.udea.dainxor.banco2025.types;

import jakarta.validation.constraints.NotNull;

public record ResponseBody<DTO>(DTO data, @NotNull String message) {
    public static <DTO> ResponseBody<DTO> of(DTO data) {
        return new ResponseBody<>(data, "");
    }
    public static <DTO> ResponseBody<DTO> empty(String message) {
        if (message == null) {
            message = "";
        }
        return new ResponseBody<>(null, message);
    }
}
