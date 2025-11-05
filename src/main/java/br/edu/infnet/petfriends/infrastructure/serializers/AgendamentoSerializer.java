package br.edu.infnet.petfriends.infrastructure.serializers;

import br.edu.infnet.petfriends.domain.Agendamento;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AgendamentoSerializer extends StdSerializer<Agendamento> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public AgendamentoSerializer() {
        super(Agendamento.class);
    }

    @Override
    public void serialize(Agendamento agendamento, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", agendamento.getId());
        jgen.writeNumberField("clienteId", agendamento.getClienteId());
        jgen.writeNumberField("profissionalId", agendamento.getProfissionalId());
        jgen.writeStringField("dataAgendamento", agendamento.getDataAgendamento().format(DATE_FORMATTER));
        jgen.writeStringField("horario", agendamento.getHorario().format(TIME_FORMATTER));
        jgen.writeStringField("tipoServico", agendamento.getTipoServico().toString());
        jgen.writeStringField("status", agendamento.getStatus().toString());
        jgen.writeNumberField("valor", agendamento.getValor().getQuantia().doubleValue());
        jgen.writeStringField("observacoes", agendamento.getObservacoes());
        jgen.writeEndObject();
    }
}