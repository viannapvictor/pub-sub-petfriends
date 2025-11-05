package br.edu.infnet.petfriends.infrastructure.serializers;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoCriadoEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AgendamentoCriadoEventSerializer extends StdSerializer<AgendamentoCriadoEvent> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public AgendamentoCriadoEventSerializer() {
        super(AgendamentoCriadoEvent.class);
    }

    @Override
    public void serialize(AgendamentoCriadoEvent evento, JsonGenerator jgen, SerializerProvider provider)
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

        jgen.writeNumberField("clienteId", evento.getClienteId());
        jgen.writeNumberField("profissionalId", evento.getProfissionalId());
        jgen.writeStringField("dataAgendamento", evento.getDataAgendamento().format(DATE_FORMATTER));
        jgen.writeStringField("horario", evento.getHorario().format(TIME_FORMATTER));
        jgen.writeStringField("tipoServico", evento.getTipoServico());
        jgen.writeNumberField("valor", evento.getValor());

        jgen.writeEndObject();
    }
}