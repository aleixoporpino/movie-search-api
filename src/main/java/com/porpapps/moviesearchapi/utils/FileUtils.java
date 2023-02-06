package com.porpapps.moviesearchapi.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

@Slf4j
public final class FileUtils {
    private static final int LINE_COUNT = 296;

    public static String randomLineFrom(String filePath) {
        final var random = new Random();
        int randomLine = random.nextInt(LINE_COUNT) + 1;

        try (final var stream = FileUtils.class.getResourceAsStream(filePath)) {
            if (stream == null) {
                log.error("No file found under {}", filePath);
                return "";
            }
            try (final var reader = new BufferedReader(new InputStreamReader(stream))) {
                return reader.lines().toList().get(randomLine);
            }
        } catch (IOException e) {
            log.error("Error when reading line {} in file {}. ERROR: {}", randomLine, filePath, e.getMessage());
            return "";
        }
    }
}
