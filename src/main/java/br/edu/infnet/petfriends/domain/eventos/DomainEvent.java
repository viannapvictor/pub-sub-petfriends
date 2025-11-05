package br.edu.infnet.petfriends.domain.eventos;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "tipoEvento",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AgendamentoCriadoEvent.class, name = "AgendamentoCriado"),
        @JsonSubTypes.Type(value = AgendamentoConfirmadoEvent.class, name = "AgendamentoConfirmado"),
        @JsonSubTypes.Type(value = AgendamentoCanceladoEvent.class, name = "AgendamentoCancelado"),
        @JsonSubTypes.Type(value = AgendamentoConcluidoEvent.class, name = "AgendamentoConcluido")
})
public interface DomainEvent extends Serializable {

    UUID getEventId();

    LocalDateTime getOcorridoEm();

    String getTipoEvento();

    Long getAgregadoId();
}
