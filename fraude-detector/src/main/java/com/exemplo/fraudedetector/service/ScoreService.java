package com.exemplo.fraudedetector.service;

import com.exemplo.fraudedetector.dto.ScoreResponse;
import com.exemplo.fraudedetector.dto.TransacaoRequest;
import com.exemplo.fraudedetector.model.Transacao;
import com.exemplo.fraudedetector.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ScoreService {

    private final TransacaoRepository transacaoRepository;

    @Autowired
    public ScoreService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public ScoreResponse calcularScore(TransacaoRequest transacao) throws ExecutionException, InterruptedException {
        int score = 0;

        // Regra 1: valor alto
        if (transacao.getValor() > 3000) {
            score += 20;
        }

        // Regra 2: horário de madrugada
        int hora = Integer.parseInt(transacao.getHora().split(":")[0]);
        if (hora >= 0 && hora <= 5) {
            score += 15;
        }

        // Buscar última transação do usuário para comparar
        Transacao ultima = transacaoRepository.buscarUltimaTransacao(transacao.getUsuarioId());

        if (ultima != null) {
            // Regra 3: cidade diferente da última compra
            if (!ultima.getCidade().equalsIgnoreCase(transacao.getCidade())) {
                score += 25;
            }

            // Regra 4: dispositivo diferente do último usado
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

        // Salvar a transação atual no Firestore para servir de histórico nas próximas
        Transacao novaTransacao = new Transacao();
        novaTransacao.setUsuarioId(transacao.getUsuarioId());
        novaTransacao.setValor(transacao.getValor());
        novaTransacao.setCidade(transacao.getCidade());
        novaTransacao.setHora(transacao.getHora());
        novaTransacao.setDispositivo(transacao.getDispositivo());
        novaTransacao.setScore(score);
        novaTransacao.setRisco(risco);

        transacaoRepository.salvar(novaTransacao);

        return new ScoreResponse(score, risco);
    }
}


