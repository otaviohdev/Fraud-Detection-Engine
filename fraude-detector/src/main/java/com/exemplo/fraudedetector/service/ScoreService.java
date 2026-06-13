package com.exemplo.fraudedetector.service;

import com.exemplo.fraudedetector.dto.ScoreResponse;
import com.exemplo.fraudedetector.dto.TransacaoRequest;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    public ScoreResponse calcularScore(TransacaoRequest transacao) {
        int score = 0;

        if (transacao.getValor() > 3000) {
            score += 20;
        }

        int hora = Integer.parseInt(transacao.getHora().split(":")[0]);
        if (hora >= 0 && hora <= 5) {
            score += 15;
        }

        if ("Android".equalsIgnoreCase(transacao.getDispositivo()) ||
            "Desconhecido".equalsIgnoreCase(transacao.getDispositivo())) {
            score += 30;
        }

        String risco;
        if (score >= 50) {
            risco = "ALTO";
        } else if (score >= 20) {
            risco = "MEDIO";
        } else {
            risco = "BAIXO";
        }

        return new ScoreResponse(score, risco);
    }
}
