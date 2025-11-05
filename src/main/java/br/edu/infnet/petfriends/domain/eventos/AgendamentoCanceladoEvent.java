package br.edu.infnet.petfriends.domain.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.time.LocalDateTime;
import java.util.UUID;

public class AgendamentoCanceladoEvent implements DomainEvent {

    private static final long serialVersionUID = 1L;

    private final UUID eventId;
    private final LocalDateTime ocorridoEm;
    private final Long agendamentoId;
    private final String motivoCancelamento;
    private final boolean canceladoPeloCliente;

    public AgendamentoCanceladoEvent(Long agendamentoId, String motivoCancelamento, boolean canceladoPeloCliente) {
        this.eventId = UUID.randomUUID();
        this.ocorridoEm = LocalDateTime.now();
        this.agendamentoId = agendamentoId;
        this.motivoCancelamento = motivoCancelamento;
        this.canceladoPeloCliente = canceladoPeloCliente;
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
        return "AgendamentoCancelado";
    }

    @Override
    @JsonProperty("agendamentoId")
    public Long getAgregadoId() {
        return agendamentoId;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public boolean isCanceladoPeloCliente() {
        return canceladoPeloCliente;
    }

    @Override
    public String toString() {
        return "AgendamentoCanceladoEvent{" +
                "eventId=" + eventId +
                ", ocorridoEm=" + ocorridoEm +
                ", agendamentoId=" + agendamentoId +
                ", motivoCancelamento='" + motivoCancelamento + '\'' +
                ", canceladoPeloCliente=" + canceladoPeloCliente +
                '}';
    }
}