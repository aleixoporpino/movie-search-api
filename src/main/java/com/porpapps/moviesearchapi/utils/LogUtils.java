package com.porpapps.moviesearchapi.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

import java.util.Objects;

public final class LogUtils {
    public static void info(Logger log, HttpServletRequest request, String message) {
        final var platform = Objects.requireNonNullElse(request.getHeader("sec-ch-ua-platform"), "");
        final var userAgent = Objects.requireNonNullElse(request.getHeader("User-Agent"), "");
        log.info("{} \n Platform : {} \n User agent : {} ", message, platform, userAgent);
    }
}
