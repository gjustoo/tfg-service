package com.gabriel.tfg.utils;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    public static final int STRENGTH = 10;
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder(PasswordUtils.STRENGTH,
            new SecureRandom());

    public static boolean test(String password, String rawPassword) {

        return ENCODER.matches(rawPassword, password);
    }

    public static String hash(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

}
