package com.muravev.samokatimmonolit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatusCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Не авторизован", "Неавторизованный доступ"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Нет доступа"),

    INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Инвентарь не найден"),

    INVENTORY_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "Модель инвентаря не найдена"),
    INVENTORY_MODEL_CANNOT_REMOVED(HttpStatus.BAD_REQUEST, "Модель инвентаря не может быть удалена"),

    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Пользователь уже существует"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Пользователь не найден"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String description;

    StatusCode(HttpStatus httpStatus, String message) {
        this(httpStatus, message, null);
    }
}
