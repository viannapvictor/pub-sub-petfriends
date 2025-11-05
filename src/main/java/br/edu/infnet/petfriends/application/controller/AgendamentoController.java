package br.edu.infnet.petfriends.application.controller;

import br.edu.infnet.petfriends.domain.Agendamento;
import br.edu.infnet.petfriends.domain.eventos.DomainEvent;
import br.edu.infnet.petfriends.infrastructure.AgendamentoRepository;
import br.edu.infnet.petfriends.infrastructure.messaging.AgendamentoMessageService;
import br.edu.infnet.petfriends.infrastructure.persistence.EventStoreService;
import br.edu.infnet.petfriends.valueobject.ValorMonetario;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    private static final Logger LOG = LoggerFactory.getLogger(AgendamentoController.class);

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private EventStoreService eventStoreService;

    @Autowired
    private PubSubTemplate pubSubTemplate;

    @Autowired
    private JacksonPubSubMessageConverter messageConverter;

    @Autowired
    private AgendamentoMessageService messageService;

    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> criarAgendamento(@RequestBody AgendamentoRequest request) {
        try {
            Agendamento agendamento = new Agendamento(
                    request.getClienteId(),
                    request.getProfissionalId(),
                    request.getDataAgendamento(),
                    request.getHorario(),
                    request.getTipoServico(),
                    new ValorMonetario(request.getValor())
            );

            Agendamento salvo = repository.save(agendamento);

            repository.flush();

            processarEventos(salvo);

            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Agendamento criado com sucesso");
            response.put("agendamento", toDTO(salvo));

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "sucesso", false,
                    "erro", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "sucesso", false,
                    "erro", "Erro ao criar agendamento: " + e.getMessage()
            ));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        List<Agendamento> agendamentos = repository.findAll();

        Map<String, Object> response = new HashMap<>();
        response.put("sucesso", true);
        response.put("total", agendamentos.size());
        response.put("agendamentos", agendamentos.stream().map(this::toDTO).toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Optional<Agendamento> agendamento = repository.findById(id);

        if (agendamento.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("agendamento", toDTO(agendamento.get()));
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "sucesso", false,
                "erro", "Agendamento não encontrado"
        ));
    }

    @PutMapping("/{id}/confirmar")
    @Transactional
    public ResponseEntity<Map<String, Object>> confirmar(@PathVariable Long id) {
        try {
            Optional<Agendamento> opt = repository.findById(id);

            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "sucesso", false,
                        "erro", "Agendamento não encontrado"
                ));
            }

            Agendamento agendamento = opt.get();
            agendamento.confirmar();

            Agendamento salvo = repository.save(agendamento);
            processarEventos(salvo);

            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Agendamento confirmado com sucesso");
            response.put("agendamento", toDTO(salvo));

            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "sucesso", false,
                    "erro", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}/cancelar")
    @Transactional
    public ResponseEntity<Map<String, Object>> cancelar(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        try {
            Optional<Agendamento> opt = repository.findById(id);

            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "sucesso", false,
                        "erro", "Agendamento não encontrado"
                ));
            }

            String motivo = (String) body.getOrDefault("motivo", "");
            Boolean canceladoPeloCliente = (Boolean) body.getOrDefault("canceladoPeloCliente", true);

            Agendamento agendamento = opt.get();
            agendamento.cancelar(motivo, canceladoPeloCliente);

            Agendamento salvo = repository.save(agendamento);
            processarEventos(salvo);

            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Agendamento cancelado com sucesso");
            response.put("agendamento", toDTO(salvo));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "sucesso", false,
                    "erro", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}/concluir")
    @Transactional
    public ResponseEntity<Map<String, Object>> concluir(@PathVariable Long id) {
        try {
            Optional<Agendamento> opt = repository.findById(id);

            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "sucesso", false,
                        "erro", "Agendamento não encontrado"
                ));
            }

            Agendamento agendamento = opt.get();
            agendamento.concluir();

            Agendamento salvo = repository.save(agendamento);
            processarEventos(salvo);

            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Agendamento concluído com sucesso");
            response.put("agendamento", toDTO(salvo));

            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "sucesso", false,
                    "erro", e.getMessage()
            ));
        }
    }

    @PutMapping("/{id}/desconto")
    @Transactional
    public ResponseEntity<Map<String, Object>> aplicarDesconto(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {

        try {
            Optional<Agendamento> opt = repository.findById(id);

            if (opt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "sucesso", false,
                        "erro", "Agendamento não encontrado"
                ));
            }

            Number percentualNum = (Number) body.get("percentual");
            if (percentualNum == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "sucesso", false,
                        "erro", "Percentual de desconto é obrigatório"
                ));
            }

            Agendamento agendamento = opt.get();
            agendamento.aplicarDesconto(new java.math.BigDecimal(percentualNum.toString()));

            Agendamento salvo = repository.save(agendamento);

            Map<String, Object> response = new HashMap<>();
            response.put("sucesso", true);
            response.put("mensagem", "Desconto aplicado com sucesso");
            response.put("agendamento", toDTO(salvo));

            return ResponseEntity.ok(response);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "sucesso", false,
                    "erro", e.getMessage()
            ));
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Map<String, Object>> buscarPorCliente(@PathVariable Long clienteId) {
        List<Agendamento> agendamentos = repository.findByClienteId(clienteId);

        Map<String, Object> response = new HashMap<>();
        response.put("sucesso", true);
        response.put("clienteId", clienteId);
        response.put("total", agendamentos.size());
        response.put("agendamentos", agendamentos.stream().map(this::toDTO).toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<Map<String, Object>> buscarPorProfissional(@PathVariable Long profissionalId) {
        List<Agendamento> agendamentos = repository.findByProfissionalId(profissionalId);

        Map<String, Object> response = new HashMap<>();
        response.put("sucesso", true);
        response.put("profissionalId", profissionalId);
        response.put("total", agendamentos.size());
        response.put("agendamentos", agendamentos.stream().map(this::toDTO).toList());

        return ResponseEntity.ok(response);
    }

    private void processarEventos(Agendamento agendamento) {
        List<DomainEvent> eventos = agendamento.getDomainEvents();

        for (DomainEvent evento : eventos) {
            try {
                eventStoreService.salvar(evento, "Agendamento", "SYSTEM");
                messageService.publicarEvento(pubSubTemplate, messageConverter, evento);
            } catch (Exception e) {
                LOG.error("Erro ao processar evento {}: {}", evento.getTipoEvento(), e.getMessage(), e);
            }
        }

        agendamento.clearDomainEvents();
    }

    private Map<String, Object> toDTO(Agendamento agendamento) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", agendamento.getId());
        dto.put("clienteId", agendamento.getClienteId());
        dto.put("profissionalId", agendamento.getProfissionalId());
        dto.put("dataAgendamento", agendamento.getDataAgendamento().toString());
        dto.put("horario", agendamento.getHorario().toString());
        dto.put("tipoServico", agendamento.getTipoServico().toString());
        dto.put("status", agendamento.getStatus().toString());
        dto.put("valor", agendamento.getValor().getQuantia().doubleValue());
        dto.put("observacoes", agendamento.getObservacoes());
        return dto;
    }
}