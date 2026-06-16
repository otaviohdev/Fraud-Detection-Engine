package com.exemplo.fraudedetector.controller;

import com.exemplo.fraudedetector.dto.ScoreResponse;
import com.exemplo.fraudedetector.dto.TransacaoRequest;
import com.exemplo.fraudedetector.model.Transacao;
import com.exemplo.fraudedetector.repository.TransacaoRepository;
import com.exemplo.fraudedetector.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final ScoreService scoreService;
    private final TransacaoRepository transacaoRepository;

    public TransacaoController(ScoreService scoreService, TransacaoRepository transacaoRepository) {
        this.scoreService = scoreService;
        this.transacaoRepository = transacaoRepository;
    }

    @PostMapping("/analisar")
    public ScoreResponse analisar(@Valid @RequestBody TransacaoRequest transacao) throws Exception {
        return scoreService.calcularScore(transacao);
    }

    @GetMapping("/listar")
    public List<Transacao> listar() throws Exception {
        return transacaoRepository.listarTodas();
    }
}
