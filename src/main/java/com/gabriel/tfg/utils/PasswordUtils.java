package com.gabriel.tfg.utils;

import java.security.SecureRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    public static final int STRENGTH = 10;
    private BCryptPasswordEncoder encoder;

    public PasswordUtils() {
        this.encoder = new BCryptPasswordEncoder(PasswordUtils.STRENGTH, new SecureRandom());
    }

    public static void main(String[] args) {

        BCryptPasswordEncoder coder = new BCryptPasswordEncoder(PasswordUtils.STRENGTH, new SecureRandom());
        String encodedPassword = coder.encode("hola como estas");

        System.out.println(encodedPassword);

        System.out.println();

    }

    public boolean test(String password, String rawPassword) {
        return this.encoder.matches(rawPassword, password);
    }

    public String hash(String rawPassword) {
        return this.encoder.encode(rawPassword);
    }

}
