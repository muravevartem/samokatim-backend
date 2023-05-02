package com.muravev.samokatimmonolit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StatusCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Не авторизован", "Неавторизованный доступ"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Нет доступа"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Не валидный запрос"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера"),

    INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Инвентарь не найден"),

    INVENTORY_MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, "Модель инвентаря не найдена"),
    INVENTORY_MODEL_CANNOT_REMOVED(HttpStatus.BAD_REQUEST, "Модель инвентаря не может быть удалена"),

    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Пользователь уже существует"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Пользователь не найден"),
    USER_INVITE_IS_EXPIRED(HttpStatus.BAD_REQUEST, "Код устарел", "Запросите новый код"),
    USER_INVALID_INVITE_CODE(HttpStatus.BAD_REQUEST, "Неверный код", "Перепроверьте или запросите новый код"),

    ORGANIZATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Организация не найдена"),
    ORGANIZATION_INVALID_INN(HttpStatus.BAD_REQUEST, "Не валидный ИНН"),
    ORGANIZATION_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Организация уже существует")
    ;

    private final HttpStatus httpStatus;
    private final String message;
    private final String description;

    StatusCode(HttpStatus httpStatus, String message) {
        this(httpStatus, message, null);
    }
}
