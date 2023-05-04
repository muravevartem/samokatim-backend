package com.muravev.samokatimmonolit.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    public static String normal(String raw) {
        if (raw == null)
            return null;

        return raw.trim()
                .strip();
    }
}
