package br.edu.infnet.petfriends.domain.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class AgendamentoCriadoEvent implements DomainEvent {

    private static final long serialVersionUID = 1L;
    private final UUID eventId;
    private final LocalDateTime ocorridoEm;
    private final Long agendamentoId;
    private final Long clienteId;
    private final Long profissionalId;
    private final LocalDate dataAgendamento;
    private final LocalTime horario;
    private final String tipoServico;
    private final BigDecimal valor;

    public AgendamentoCriadoEvent(
            Long agendamentoId,
            Long clienteId,
            Long profissionalId,
            LocalDate dataAgendamento,
            LocalTime horario,
            String tipoServico,
            BigDecimal valor
    ) {
        this.eventId = UUID.randomUUID();
        this.ocorridoEm = LocalDateTime.now();
        this.agendamentoId = agendamentoId;
        this.clienteId = clienteId;
        this.profissionalId = profissionalId;
        this.dataAgendamento = dataAgendamento;
        this.horario = horario;
        this.tipoServico = tipoServico;
        this.valor = valor;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public LocalDateTime getOcorridoEm() {
        return ocorridoEm;
    }

    @Override
    @JsonProperty(access = Access.READ_ONLY)
    public String getTipoEvento() {
        return "AgendamentoCriado";
    }

    @Override
    @JsonProperty("agendamentoId")
    public Long getAgregadoId() {
        return agendamentoId;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "AgendamentoCriadoEvent{" +
                "eventId=" + eventId +
                ", ocorridoEm=" + ocorridoEm +
                ", agendamentoId=" + agendamentoId +
                ", clienteId=" + clienteId +
                ", profissionalId=" + profissionalId +
                ", dataAgendamento=" + dataAgendamento +
                ", horario=" + horario +
                ", tipoServico='" + tipoServico + '\'' +
                ", valor=" + valor +
                '}';
    }
}