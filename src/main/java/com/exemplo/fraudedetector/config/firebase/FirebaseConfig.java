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

            // Lê direto das variáveis de ambiente do sistema (funciona no Railway)
            String credenciaisBase64 = System.getenv("FIREBASE_CREDENTIALS");

            if (credenciaisBase64 != null && !credenciaisBase64.isEmpty()) {
                // Produção: decodifica o Base64 e usa como stream
                byte[] decoded = Base64.getDecoder().decode(credenciaisBase64.trim());
                serviceAccount = new ByteArrayInputStream(decoded);
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
