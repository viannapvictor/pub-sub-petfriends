package br.edu.infnet.petfriends.infrastructure.deserializers;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoCriadoEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AgendamentoCriadoEventDeserializer extends StdDeserializer<AgendamentoCriadoEvent> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public AgendamentoCriadoEventDeserializer() {
        super(AgendamentoCriadoEvent.class);
    }

    @Override
    public AgendamentoCriadoEvent deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        Long agendamentoId = node.has("agendamentoId") && !node.get("agendamentoId").isNull()
                ? node.get("agendamentoId").asLong()
                : null;

        Long clienteId = node.get("clienteId").asLong();
        Long profissionalId = node.get("profissionalId").asLong();

        LocalDate dataAgendamento = LocalDate.parse(
                node.get("dataAgendamento").asText(),
                DATE_FORMATTER
        );

        LocalTime horario = LocalTime.parse(
                node.get("horario").asText(),
                TIME_FORMATTER
        );

        String tipoServico = node.get("tipoServico").asText();
        BigDecimal valor = node.get("valor").decimalValue();

        return new AgendamentoCriadoEvent(
                agendamentoId,
                clienteId,
                profissionalId,
                dataAgendamento,
                horario,
                tipoServico,
                valor
        );
    }
}