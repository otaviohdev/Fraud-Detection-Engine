package com.exemplo.fraudedetector.dto;

import jakarta.validation.constraints.*;

public class TransacaoRequest {

    @NotBlank(message = "O usuarioId é obrigatório")
    @Size(max = 50, message = "usuarioId excede o tamanho máximo permitido")
    private String usuarioId;

    @NotNull(message = "O valor é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @DecimalMax(value = "1000000.00", message = "O valor excede o limite permitido")
    private Double valor;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 100, message = "Cidade excede o tamanho máximo permitido")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s-]+$", message = "Cidade contém caracteres inválidos")
    private String cidade;

    @NotBlank(message = "A hora é obrigatória")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "Hora deve estar no formato HH:mm")
    private String hora;

    @NotBlank(message = "O dispositivo é obrigatório")
    @Size(max = 50, message = "Dispositivo excede o tamanho máximo permitido")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Dispositivo contém caracteres inválidos")
    private String dispositivo;

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getDispositivo() { return dispositivo; }
    public void setDispositivo(String dispositivo) { this.dispositivo = dispositivo; }
}
