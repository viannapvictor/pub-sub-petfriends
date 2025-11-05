package br.edu.infnet.petfriends.infrastructure.deserializers;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoConcluidoEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class AgendamentoConcluidoEventDeserializer extends StdDeserializer<AgendamentoConcluidoEvent> {

    public AgendamentoConcluidoEventDeserializer() {
        super(AgendamentoConcluidoEvent.class);
    }

    @Override
    public AgendamentoConcluidoEvent deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        Long agendamentoId = node.has("agendamentoId") && !node.get("agendamentoId").isNull()
                ? node.get("agendamentoId").asLong()
                : null;

        Long profissionalId = node.get("profissionalId").asLong();

        return new AgendamentoConcluidoEvent(agendamentoId, profissionalId);
    }
}