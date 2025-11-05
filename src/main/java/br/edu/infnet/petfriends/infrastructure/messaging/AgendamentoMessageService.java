package br.edu.infnet.petfriends.infrastructure.messaging;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoCanceladoEvent;
import br.edu.infnet.petfriends.domain.eventos.AgendamentoCriadoEvent;
import br.edu.infnet.petfriends.domain.eventos.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.google.cloud.spring.pubsub.support.converter.ConvertedBasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoMessageService {

    private static final Logger LOG = LoggerFactory.getLogger(AgendamentoMessageService.class);

    private static final String TOPIC_NAME = "vic-test-topic";
    private static final String SUBSCRIPTION_NAME = "vic-test-topic-sub";

    @Bean
    public JacksonPubSubMessageConverter jacksonPubSubMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return new JacksonPubSubMessageConverter(objectMapper);
    }

    @Bean
    public MessageChannel inputMessageChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter inboundChannelAdapter(
            @Qualifier("inputMessageChannel") MessageChannel messageChannel,
            PubSubTemplate pubSubTemplate,
            JacksonPubSubMessageConverter messageConverter) {

        pubSubTemplate.setMessageConverter(messageConverter);

        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(
                pubSubTemplate,
                SUBSCRIPTION_NAME
        );

        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.MANUAL);
        adapter.setPayloadType(DomainEvent.class);

        LOG.info("Pub/Sub Subscriber configurado:");
        LOG.info("Topic: {}", TOPIC_NAME);
        LOG.info("Subscription: {}", SUBSCRIPTION_NAME);
        LOG.info("Aceitando múltiplos tipos de Domain Events");

        return adapter;
    }

    @ServiceActivator(inputChannel = "inputMessageChannel")
    public void messageReceiver(
            DomainEvent evento,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) ConvertedBasicAcknowledgeablePubsubMessage<DomainEvent> message) {

        try {
            LOG.info("Evento recebido do Pub/Sub: {} (ID: {})", evento.getTipoEvento(), evento.getAgregadoId());
            LOG.debug("Event ID: {}, Ocorrido em: {}", evento.getEventId(), evento.getOcorridoEm());

            if (evento instanceof AgendamentoCriadoEvent) {
                AgendamentoCriadoEvent criado = (AgendamentoCriadoEvent) evento;
                LOG.debug("Detalhes - Cliente: {}, Profissional: {}, Data: {}, Servico: {}, Valor: {}",
                        criado.getClienteId(), criado.getProfissionalId(), criado.getDataAgendamento(),
                        criado.getTipoServico(), criado.getValor());
            } else if (evento instanceof AgendamentoCanceladoEvent) {
                AgendamentoCanceladoEvent cancelado = (AgendamentoCanceladoEvent) evento;
                LOG.info("Cancelamento - Motivo: {}, Cliente: {}", 
                        cancelado.getMotivoCancelamento(), cancelado.isCanceladoPeloCliente());
            }

            message.ack();
            LOG.debug("Evento processado e confirmado (ACK)");

        } catch (Exception e) {
            LOG.error("Erro ao processar mensagem do Pub/Sub: {}", evento.getTipoEvento(), e);
            message.nack();
        }
    }

    public void publicarEvento(PubSubTemplate pubSubTemplate,
                               JacksonPubSubMessageConverter messageConverter,
                               DomainEvent evento) {
        pubSubTemplate.setMessageConverter(messageConverter);
        pubSubTemplate.publish(TOPIC_NAME, evento);

        LOG.info("Evento publicado no Pub/Sub: {} (ID: {})", evento.getTipoEvento(), evento.getAgregadoId());
        LOG.debug("Event ID: {}, Topic: {}, Ocorrido em: {}", evento.getEventId(), TOPIC_NAME, evento.getOcorridoEm());
    }
}