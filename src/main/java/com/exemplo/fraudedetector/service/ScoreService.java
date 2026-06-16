package com.exemplo.fraudedetector.service;

import com.exemplo.fraudedetector.dto.ScoreResponse;
import com.exemplo.fraudedetector.dto.TransacaoRequest;
import com.exemplo.fraudedetector.model.Transacao;
import com.exemplo.fraudedetector.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ScoreService {

    private final TransacaoRepository transacaoRepository;

    // RestTemplate é a classe do Spring que faz requisições HTTP
    // Usamos ela para chamar o servidor Python
    private final RestTemplate restTemplate = new RestTemplate();

    // URL do servidor Python rodando localmente
    private static final String PYTHON_URL = "http://localhost:5000/prever";

    @Autowired
    public ScoreService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public ScoreResponse calcularScore(TransacaoRequest transacao)
            throws ExecutionException, InterruptedException {

        int score = 0;

        // Regra 1: valor alto — transações acima de R$3000 somam 20 pontos
        if (transacao.getValor() > 3000) {
            score += 20;
        }

        // Regra 2: horário de madrugada — compras entre 00h e 05h somam 15 pontos
        int hora = Integer.parseInt(transacao.getHora().split(":")[0]);
        if (hora >= 0 && hora <= 5) {
            score += 15;
        }

        // Busca a última transação desse usuário no Firestore para comparar
        Transacao ultima = transacaoRepository.buscarUltimaTransacao(transacao.getUsuarioId());

        if (ultima != null) {
            // Regra 3: cidade diferente da última compra — soma 25 pontos
            if (!ultima.getCidade().equalsIgnoreCase(transacao.getCidade())) {
                score += 25;
            }

            // Regra 4: dispositivo diferente do último usado — soma 30 pontos
            if (!ultima.getDispositivo().equalsIgnoreCase(transacao.getDispositivo())) {
                score += 30;
            }
        }

        // Classifica o risco com base no score total
        String risco;
        if (score >= 50) {
            risco = "ALTO";
        } else if (score >= 20) {
            risco = "MEDIO";
        } else {
            risco = "BAIXO";
        }

        // Chama o modelo de Machine Learning no servidor Python
        String labelML = chamarModeloML(transacao.getValor(), hora, score);

        // Salva a transação no Firestore para servir de histórico nas próximas
        Transacao novaTransacao = new Transacao();
        novaTransacao.setUsuarioId(transacao.getUsuarioId());
        novaTransacao.setValor(transacao.getValor());
        novaTransacao.setCidade(transacao.getCidade());
        novaTransacao.setHora(transacao.getHora());
        novaTransacao.setDispositivo(transacao.getDispositivo());
        novaTransacao.setScore(score);
        novaTransacao.setRisco(risco);
        transacaoRepository.salvar(novaTransacao);

        // Retorna o score, o risco e o resultado do ML juntos
        return new ScoreResponse(score, risco, labelML);
    }

    // Método privado que chama o servidor Python e retorna o resultado do ML
    private String chamarModeloML(double valor, int hora, int score) {
        try {
            // Monta o JSON que vai ser enviado para o Python
            Map<String, Object> payload = new HashMap<>();
            payload.put("valor", valor);
            payload.put("hora", hora);
            payload.put("score", score);

            // Define o cabeçalho dizendo que o conteúdo é JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Junta o body com os headers
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            // Faz o POST para o servidor Python e recebe a resposta
            ResponseEntity<Map> response = restTemplate.postForEntity(
                PYTHON_URL, request, Map.class
            );

            // Pega o campo "label" da resposta ("FRAUDE" ou "NORMAL")
            return (String) response.getBody().get("label");

        } catch (Exception e) {
            // Se o Python não estiver rodando, não quebra o sistema
            // Apenas informa que o ML está indisponível
            return "ML_INDISPONIVEL";
        }
    }
}