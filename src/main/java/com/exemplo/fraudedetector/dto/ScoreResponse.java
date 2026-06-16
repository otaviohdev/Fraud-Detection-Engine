package com.exemplo.fraudedetector.dto;

public class ScoreResponse {

    private int score;
    private String risco;
    private String resultadoML;
    private String explicacao;

    public ScoreResponse(int score, String risco, String resultadoML, String explicacao) {
        this.score = score;
        this.risco = risco;
        this.resultadoML = resultadoML;
        this.explicacao = explicacao;
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    public String getResultadoML() { return resultadoML; }
    public void setResultadoML(String resultadoML) { this.resultadoML = resultadoML; }

    public String getExplicacao() { return explicacao; }
    public void setExplicacao(String explicacao) { this.explicacao = explicacao; }
}