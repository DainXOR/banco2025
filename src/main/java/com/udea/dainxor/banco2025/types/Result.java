package com.udea.dainxor.banco2025.types;

public record Result<S, E> (S data, E error) {
    public static <S, E> Result<S, E> success(S data) {
        return new Result<>(data, null);
    }

    public static <S, E> Result<S, E> error(E error) {
        return new Result<>(null, error);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isError() {
        return error != null;
    }
}

