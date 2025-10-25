package com.udea.dainxor.banco2025.types;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponse<DTO> extends ResponseEntity<ResponseBody<DTO>> {
    public HttpResponse(ResponseBody<DTO> body, org.springframework.http.HttpStatus status) {
        super(body, status);
    }

    public static <DTO>HttpResponse<DTO> success(DTO data, HttpStatus status) {
        return new HttpResponse<>(ResponseBody.of(data), status);
    }
    public static <DTO>HttpResponse<DTO> error(String errorMessage, HttpStatus status) {
        return new HttpResponse<>(ResponseBody.empty(errorMessage), status);
    }

    public static <DTO> HttpResponse<DTO> fromResult(Result<DTO, String> result, HttpStatus successStatus, HttpStatus errorStatus) {
        if (result.isError()) {
            return new HttpResponse<>(ResponseBody.empty(result.error()), errorStatus);
        } else {
            return new HttpResponse<>(ResponseBody.of(result.data()), successStatus);
        }
    }

}
