package br.edu.infnet.petfriends.infrastructure.deserializers;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoCanceladoEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class AgendamentoCanceladoEventDeserializer extends StdDeserializer<AgendamentoCanceladoEvent> {

    public AgendamentoCanceladoEventDeserializer() {
        super(AgendamentoCanceladoEvent.class);
    }

    @Override
    public AgendamentoCanceladoEvent deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        Long agendamentoId = node.has("agendamentoId") && !node.get("agendamentoId").isNull()
                ? node.get("agendamentoId").asLong()
                : null;

        String motivoCancelamento = node.get("motivoCancelamento").asText();
        boolean canceladoPeloCliente = node.get("canceladoPeloCliente").asBoolean();

        return new AgendamentoCanceladoEvent(agendamentoId, motivoCancelamento, canceladoPeloCliente);
    }
}