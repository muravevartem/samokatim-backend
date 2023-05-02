package com.muravev.samokatimmonolit.error.data;

public record BadField(
        String fieldName,
        String message
) {
}
