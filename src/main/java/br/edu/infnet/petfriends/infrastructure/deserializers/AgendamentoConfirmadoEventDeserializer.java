package br.edu.infnet.petfriends.infrastructure.deserializers;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoConfirmadoEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class AgendamentoConfirmadoEventDeserializer extends StdDeserializer<AgendamentoConfirmadoEvent> {

    public AgendamentoConfirmadoEventDeserializer() {
        super(AgendamentoConfirmadoEvent.class);
    }

    @Override
    public AgendamentoConfirmadoEvent deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        Long agendamentoId = node.has("agendamentoId") && !node.get("agendamentoId").isNull()
                ? node.get("agendamentoId").asLong()
                : null;

        Long profissionalId = node.get("profissionalId").asLong();

        return new AgendamentoConfirmadoEvent(agendamentoId, profissionalId);
    }
}