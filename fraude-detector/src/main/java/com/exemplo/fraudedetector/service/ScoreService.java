package com.exemplo.fraudedetector.service;

import com.exemplo.fraudedetector.dto.ScoreResponse;
import com.exemplo.fraudedetector.dto.TransacaoRequest;
import com.exemplo.fraudedetector.model.Transacao;
import com.exemplo.fraudedetector.repository.TransacaoRepository;
import com.exemplo.fraudedetector.ia.AnalistaIA;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ScoreService {

    private final TransacaoRepository transacaoRepository;
    private final AnalistaIA analistaIA;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PYTHON_URL = "http://localhost:5000/prever";

    public ScoreService(TransacaoRepository transacaoRepository, AnalistaIA analistaIA) {
    this.transacaoRepository = transacaoRepository;
    this.analistaIA = analistaIA;
}

    public ScoreResponse calcularScore(TransacaoRequest transacao)
            throws ExecutionException, InterruptedException {

        int score = 0;

        if (transacao.getValor() > 3000) {
            score += 20;
        }

        int hora = Integer.parseInt(transacao.getHora().split(":")[0]);
        if (hora >= 0 && hora <= 5) {
            score += 15;
        }

        Transacao ultima = transacaoRepository.buscarUltimaTransacao(transacao.getUsuarioId());

        if (ultima != null) {
            if (!ultima.getCidade().equalsIgnoreCase(transacao.getCidade())) {
                score += 25;
            }
            if (!ultima.getDispositivo().equalsIgnoreCase(transacao.getDispositivo())) {
                score += 30;
            }
        }

        String risco;
        if (score >= 50) {
            risco = "ALTO";
        } else if (score >= 20) {
            risco = "MEDIO";
        } else {
            risco = "BAIXO";
        }

        String labelML = chamarModeloML(transacao.getValor(), hora, score);
        String explicacao = analistaIA.explicarScore(
    transacao.getValor(), transacao.getCidade(), transacao.getHora(),
    transacao.getDispositivo(), score, risco
);

        Transacao novaTransacao = new Transacao();
        novaTransacao.setUsuarioId(transacao.getUsuarioId());
        novaTransacao.setValor(transacao.getValor());
        novaTransacao.setCidade(transacao.getCidade());
        novaTransacao.setHora(transacao.getHora());
        novaTransacao.setDispositivo(transacao.getDispositivo());
        novaTransacao.setScore(score);
        novaTransacao.setRisco(risco);
        transacaoRepository.salvar(novaTransacao);

        return new ScoreResponse(score, risco, labelML, explicacao);
    }

    private String chamarModeloML(double valor, int hora, int score) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("valor", valor);
            payload.put("hora", hora);
            payload.put("score", score);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                PYTHON_URL, request, Map.class
            );

            return (String) response.getBody().get("label");

        } catch (Exception e) {
            return "ML_INDISPONIVEL";
        }
    }
}