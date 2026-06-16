package com.exemplo.fraudedetector.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount;

            String credenciaisBase64 = System.getenv("FIREBASE_CREDENTIALS");

            // Log para debug — vai aparecer nos logs do Railway
            System.out.println("=== FIREBASE DEBUG ===");
            System.out.println("Variavel presente: " + (credenciaisBase64 != null));
            System.out.println("Variavel vazia: " + (credenciaisBase64 == null || credenciaisBase64.isEmpty()));
            if (credenciaisBase64 != null) {
                System.out.println("Primeiros 20 chars: " + credenciaisBase64.substring(0, Math.min(20, credenciaisBase64.length())));
                System.out.println("Tamanho: " + credenciaisBase64.length());
            }
            System.out.println("=== FIM DEBUG ===");

            if (credenciaisBase64 != null && !credenciaisBase64.isEmpty()) {
                byte[] decoded = Base64.getDecoder().decode(credenciaisBase64.trim());
                serviceAccount = new ByteArrayInputStream(decoded);
            } else {
                serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebase-service-account.json");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

            FirebaseApp.initializeApp(options);
        }
    }
}
