package com.exemplo.fraudedetector.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount;

            // Tenta ler o JSON direto da variável de ambiente
            String credenciaisJson = System.getenv("FIREBASE_CREDENTIALS_JSON");

            if (credenciaisJson != null && !credenciaisJson.isEmpty()) {
                // Produção: converte a string JSON para stream
                serviceAccount = new ByteArrayInputStream(
                    credenciaisJson.getBytes(StandardCharsets.UTF_8)
                );
            } else {
                // Local: lê o arquivo JSON normalmente
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

