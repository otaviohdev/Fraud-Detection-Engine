package com.exemplo.fraudedetector.dto;

public class ScoreResponse {
    private int score;
    private String risco;

    public ScoreResponse(int score, String risco) {
        this.score = score;
        this.risco = risco;
    }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }
}
