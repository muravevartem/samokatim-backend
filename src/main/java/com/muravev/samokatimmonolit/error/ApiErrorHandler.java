package com.muravev.samokatimmonolit.error;

import com.muravev.samokatimmonolit.model.out.ErrorOut;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApiErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorOut> handle(ApiException e) {
        log.error("Api error", e);
        StatusCode code = e.getCode();
        return ResponseEntity.status(code.getHttpStatus())
                .body(new ErrorOut(code, code.getMessage(), code.getDescription()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorOut> handle(MethodArgumentNotValidException e) {
        StatusCode code = StatusCode.BAD_REQUEST;
        return ResponseEntity.status(code.getHttpStatus())
                .body(new ErrorOut(code, code.getMessage(), code.getDescription()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorOut> handle(Exception e) {
        log.error("Internal server error", e);
        StatusCode code = StatusCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(code.getHttpStatus())
                .body(new ErrorOut(code, code.getMessage(), code.getDescription()));
    }
}
