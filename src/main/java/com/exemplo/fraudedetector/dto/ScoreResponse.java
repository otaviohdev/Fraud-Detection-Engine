package com.exemplo.fraudedetector.dto;

public class ScoreResponse {

    private int score;
    private String risco;
    private String resultadoML;

    public ScoreResponse(int score, String risco, String resultadoML) {
        this.score = score;
        this.risco = risco;
        this.resultadoML = resultadoML;
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    public String getResultadoML() { return resultadoML; }
    public void setResultadoML(String resultadoML) { this.resultadoML = resultadoML; }
}