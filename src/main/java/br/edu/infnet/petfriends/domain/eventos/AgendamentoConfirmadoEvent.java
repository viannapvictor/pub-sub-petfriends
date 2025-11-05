package br.edu.infnet.petfriends.domain.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.time.LocalDateTime;
import java.util.UUID;

public class AgendamentoConfirmadoEvent implements DomainEvent {

    private static final long serialVersionUID = 1L;

    private final UUID eventId;
    private final LocalDateTime ocorridoEm;
    private final Long agendamentoId;
    private final Long profissionalId;

    public AgendamentoConfirmadoEvent(Long agendamentoId, Long profissionalId) {
        this.eventId = UUID.randomUUID();
        this.ocorridoEm = LocalDateTime.now();
        this.agendamentoId = agendamentoId;
        this.profissionalId = profissionalId;
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
        return "AgendamentoConfirmado";
    }

    @Override
    @JsonProperty("agendamentoId")
    public Long getAgregadoId() {
        return agendamentoId;
    }

    public Long getAgendamentoId() {
        return agendamentoId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    @Override
    public String toString() {
        return "AgendamentoConfirmadoEvent{" +
                "eventId=" + eventId +
                ", ocorridoEm=" + ocorridoEm +
                ", agendamentoId=" + agendamentoId +
                ", profissionalId=" + profissionalId +
                '}';
    }
}