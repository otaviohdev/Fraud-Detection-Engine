package com.exemplo.fraudedetector.ia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class AnalistaIA {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEMINI_URL =
    "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=%s";

    public String explicarScore(double valor, String cidade, String hora,
                                  String dispositivo, int score, String risco) {
        try {
            String prompt = String.format(
                "Voce e um analista de fraude bancaria. Explique em ate 3 frases, " +
                "de forma clara e direta, por que esta transacao recebeu o score de risco indicado. " +
                "Dados: valor R$%.2f, cidade %s, horario %s, dispositivo %s, " +
                "score calculado %d, classificacao de risco %s.",
                valor, cidade, hora, dispositivo, score, risco
            );

            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(
                        Map.of("text", prompt)
                    ))
                )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String urlComChave = String.format(GEMINI_URL, apiKey);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                urlComChave, request, Map.class
            );

            Map body = response.getBody();
            List candidates = (List) body.get("candidates");
            Map primeiroCandidato = (Map) candidates.get(0);
            Map content = (Map) primeiroCandidato.get("content");
            List parts = (List) content.get("parts");
            Map primeiraParte = (Map) parts.get(0);

            return (String) primeiraParte.get("text");

        } catch (Exception e) {
            return "Explicacao da IA indisponivel no momento.";
        }
    }
}