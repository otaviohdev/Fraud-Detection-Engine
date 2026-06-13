package com.exemplo.fraudedetector.controller;

import com.exemplo.fraudedetector.dto.ScoreResponse;
import com.exemplo.fraudedetector.dto.TransacaoRequest;
import com.exemplo.fraudedetector.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final ScoreService scoreService;

    public TransacaoController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping("/analisar")
    public ScoreResponse analisar(@Valid @RequestBody TransacaoRequest transacao) {
        return scoreService.calcularScore(transacao);
    }
}