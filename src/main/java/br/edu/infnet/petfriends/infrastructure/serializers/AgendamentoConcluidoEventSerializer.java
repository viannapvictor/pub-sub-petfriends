package br.edu.infnet.petfriends.infrastructure.serializers;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoConcluidoEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AgendamentoConcluidoEventSerializer extends StdSerializer<AgendamentoConcluidoEvent> {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public AgendamentoConcluidoEventSerializer() {
        super(AgendamentoConcluidoEvent.class);
    }

    @Override
    public void serialize(AgendamentoConcluidoEvent evento, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();

        jgen.writeStringField("eventId", evento.getEventId().toString());
        jgen.writeStringField("tipoEvento", evento.getTipoEvento());
        jgen.writeStringField("ocorridoEm", evento.getOcorridoEm().format(DATETIME_FORMATTER));

        if (evento.getAgendamentoId() != null) {
            jgen.writeNumberField("agendamentoId", evento.getAgendamentoId());
        } else {
            jgen.writeNullField("agendamentoId");
        }

        jgen.writeNumberField("profissionalId", evento.getProfissionalId());

        jgen.writeEndObject();
    }
}