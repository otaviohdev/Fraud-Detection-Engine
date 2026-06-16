package com.exemplo.fraudedetector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FraudeDetectorApplication {

    public static void main(String[] args) {
        System.out.println("=== ENV CHECK ===");
        System.out.println("FIREBASE_CREDENTIALS_JSON presente: " + 
            (System.getenv("FIREBASE_CREDENTIALS_JSON") != null));
        System.out.println("GEMINI_API_KEY presente: " + 
            (System.getenv("GEMINI_API_KEY") != null));
        System.out.println("=== FIM ENV CHECK ===");
        SpringApplication.run(FraudeDetectorApplication.class, args);
    }
}
