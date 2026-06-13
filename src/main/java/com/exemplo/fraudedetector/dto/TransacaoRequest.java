package com.exemplo.fraudedetector.dto;

public class TransacaoRequest {
    private double valor;
    private String cidade;
    private String hora; // formato "HH:mm"
    private String dispositivo;

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getDispositivo() { return dispositivo; }
    public void setDispositivo(String dispositivo) { this.dispositivo = dispositivo; }
}
