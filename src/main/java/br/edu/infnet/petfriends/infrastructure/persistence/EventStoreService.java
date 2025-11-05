package br.edu.infnet.petfriends.infrastructure.persistence;

import br.edu.infnet.petfriends.domain.eventos.DomainEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventStoreService {

    private static final Logger LOG = LoggerFactory.getLogger(EventStoreService.class);

    private final EventStoreRepository repository;
    private final ObjectMapper objectMapper;

    public EventStoreService(EventStoreRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Transactional
    public void salvar(DomainEvent evento, String tipoAgregado, String usuario) {
        try {
            String eventData = objectMapper.writeValueAsString(evento);

            StoredEvent storedEvent = new StoredEvent(
                    evento.getEventId().toString(),
                    evento.getTipoEvento(),
                    evento.getAgregadoId(),
                    tipoAgregado,
                    evento.getOcorridoEm(),
                    eventData,
                    usuario
            );

            repository.save(storedEvent);

            LOG.info("Evento salvo no Event Store: {}", evento.getTipoEvento());
            LOG.debug("Event ID: {}", evento.getEventId());
            LOG.debug("Agregado ID: {}", evento.getAgregadoId());
            LOG.debug("Ocorrido em: {}", evento.getOcorridoEm());

        } catch (JsonProcessingException e) {
            LOG.error("Erro ao serializar evento para Event Store", e);
            throw new RuntimeException("Erro ao salvar evento no Event Store", e);
        }
    }

    @Transactional
    public void salvar(DomainEvent evento, String tipoAgregado) {
        salvar(evento, tipoAgregado, "SYSTEM");
    }

    @Transactional(readOnly = true)
    public List<StoredEvent> buscarHistorico(Long agregadoId) {
        LOG.info("Buscando histórico de eventos para agregado: {}", agregadoId);
        List<StoredEvent> eventos = repository.findByAgregadoIdOrderByOcorridoEmAsc(agregadoId);
        LOG.info("Encontrados {} eventos", eventos.size());
        return eventos;
    }

    @Transactional(readOnly = true)
    public List<StoredEvent> buscarPorTipo(String tipoEvento) {
        LOG.info("Buscando eventos do tipo: {}", tipoEvento);
        return repository.findByTipoEventoOrderByOcorridoEmDesc(tipoEvento);
    }

    @Transactional(readOnly = true)
    public List<StoredEvent> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        LOG.info("Buscando eventos entre {} e {}", inicio, fim);
        return repository.findByPeriodo(inicio, fim);
    }

    @Transactional(readOnly = true)
    public List<StoredEvent> buscarHistoricoAte(Long agregadoId, LocalDateTime ate) {
        LOG.info("Buscando histórico do agregado {} até {}", agregadoId, ate);
        return repository.findByAgregadoIdUntil(agregadoId, ate);
    }

    @Transactional(readOnly = true)
    public Long contarEventos(Long agregadoId) {
        return repository.countByAgregadoId(agregadoId);
    }

    @Transactional(readOnly = true)
    public List<StoredEvent> buscarPorTipoAgregado(String tipoAgregado) {
        LOG.info("Buscando eventos do tipo de agregado: {}", tipoAgregado);
        return repository.findByTipoAgregadoOrderByOcorridoEmDesc(tipoAgregado);
    }
}