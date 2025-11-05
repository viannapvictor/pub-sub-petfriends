package br.edu.infnet.petfriends.application.controller;

import br.edu.infnet.petfriends.domain.Agendamento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoRequest {

    private Long clienteId;
    private Long profissionalId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAgendamento;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horario;

    private Agendamento.TipoServico tipoServico;
    private BigDecimal valor;

    public AgendamentoRequest() {
    }

    public AgendamentoRequest(Long clienteId, Long profissionalId, LocalDate dataAgendamento,
                              LocalTime horario, Agendamento.TipoServico tipoServico, BigDecimal valor) {
        this.clienteId = clienteId;
        this.profissionalId = profissionalId;
        this.dataAgendamento = dataAgendamento;
        this.horario = horario;
        this.tipoServico = tipoServico;
        this.valor = valor;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Agendamento.TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(Agendamento.TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}