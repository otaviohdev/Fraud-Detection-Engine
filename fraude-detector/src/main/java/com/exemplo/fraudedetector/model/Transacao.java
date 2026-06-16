package com.exemplo.fraudedetector.model;

import java.util.Date;

public class Transacao {

    private String id;
    private String usuarioId;
    private double valor;
    private String cidade;
    private String hora;
    private String dispositivo;
    private int score;
    private String risco;
    private Date timestamp;

    public Transacao() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getDispositivo() { return dispositivo; }
    public void setDispositivo(String dispositivo) { this.dispositivo = dispositivo; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getRisco() { return risco; }
    public void setRisco(String risco) { this.risco = risco; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
