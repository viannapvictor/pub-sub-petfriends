package br.edu.infnet.petfriends.domain;

import br.edu.infnet.petfriends.domain.eventos.AgendamentoCanceladoEvent;
import br.edu.infnet.petfriends.domain.eventos.AgendamentoCriadoEvent;
import br.edu.infnet.petfriends.domain.eventos.AgendamentoConfirmadoEvent;
import br.edu.infnet.petfriends.domain.eventos.AgendamentoConcluidoEvent;
import br.edu.infnet.petfriends.domain.eventos.DomainEvent;
import br.edu.infnet.petfriends.valueobject.Horario;
import br.edu.infnet.petfriends.valueobject.ValorMonetario;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "AGENDAMENTO", catalog = "PETFRIENDS_DB", schema = "PUBLIC")
public class Agendamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "CLIENTE_ID", nullable = false)
    private Long clienteId;

    @Column(name = "PROFISSIONAL_ID", nullable = false)
    private Long profissionalId;

    @Column(name = "DATA_AGENDAMENTO", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "HORARIO", nullable = false)
    private LocalTime horario;

    @Column(name = "TIPO_SERVICO", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TipoServico tipoServico;

    @Column(name = "STATUS", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;

    @Column(name = "VALOR", precision = 10, scale = 2)
    private ValorMonetario valor;

    @Column(name = "OBSERVACOES", length = 500)
    private String observacoes;

    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();

    @Transient
    private boolean eventoJaGerado = false;

    public Agendamento() {
    }

    public Agendamento(Long clienteId, Long profissionalId, LocalDate dataAgendamento,
                       LocalTime horario, TipoServico tipoServico, ValorMonetario valor) {

        if (clienteId == null) {
            throw new IllegalArgumentException("Cliente ID não pode ser nulo");
        }
        if (profissionalId == null) {
            throw new IllegalArgumentException("Profissional ID não pode ser nulo");
        }
        if (dataAgendamento == null) {
            throw new IllegalArgumentException("Data do agendamento não pode ser nula");
        }
        if (dataAgendamento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data do agendamento não pode ser no passado");
        }
        if (horario == null) {
            throw new IllegalArgumentException("Horário não pode ser nulo");
        }
        if (tipoServico == null) {
            throw new IllegalArgumentException("Tipo de serviço não pode ser nulo");
        }
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }

        new Horario(horario);

        this.clienteId = clienteId;
        this.profissionalId = profissionalId;
        this.dataAgendamento = dataAgendamento;
        this.horario = horario;
        this.tipoServico = tipoServico;
        this.valor = valor;
        this.status = StatusAgendamento.PENDENTE;
        this.observacoes = "";

    }

    @PostPersist
    private void gerarEventoAposPersistir() {
        if (!eventoJaGerado && this.id != null) {
            this.addDomainEvent(new AgendamentoCriadoEvent(
                    this.id,
                    this.clienteId,
                    this.profissionalId,
                    this.dataAgendamento,
                    this.horario,
                    this.tipoServico.toString(),
                    this.valor.getQuantia()
            ));
            this.eventoJaGerado = true;
        }
    }

    public void confirmar() {
        if (this.status != StatusAgendamento.PENDENTE) {
            throw new IllegalStateException(
                    "Apenas agendamentos PENDENTES podem ser confirmados. Status atual: " + this.status
            );
        }

        this.status = StatusAgendamento.CONFIRMADO;

        this.addDomainEvent(new AgendamentoConfirmadoEvent(this.id, this.profissionalId));
    }

    public void cancelar(String motivo, boolean canceladoPeloCliente) {
        if (this.status == StatusAgendamento.CANCELADO) {
            throw new IllegalStateException("Agendamento já está cancelado");
        }
        if (this.status == StatusAgendamento.CONCLUIDO) {
            throw new IllegalStateException("Não é possível cancelar um agendamento já concluído");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("Motivo do cancelamento é obrigatório");
        }

        this.status = StatusAgendamento.CANCELADO;
        this.observacoes = "CANCELADO: " + motivo;

        this.addDomainEvent(new AgendamentoCanceladoEvent(
                this.id,
                motivo,
                canceladoPeloCliente
        ));
    }

    public void concluir() {
        if (this.status != StatusAgendamento.CONFIRMADO) {
            throw new IllegalStateException(
                    "Apenas agendamentos CONFIRMADOS podem ser concluídos. Status atual: " + this.status
            );
        }

        this.status = StatusAgendamento.CONCLUIDO;

        this.addDomainEvent(new AgendamentoConcluidoEvent(this.id, this.profissionalId));
    }

    public void adicionarObservacoes(String novasObservacoes) {
        if (novasObservacoes != null && !novasObservacoes.trim().isEmpty()) {
            this.observacoes = this.observacoes + "\n" + novasObservacoes;
        }
    }

    public void aplicarDesconto(java.math.BigDecimal percentual) {
        if (this.status != StatusAgendamento.PENDENTE) {
            throw new IllegalStateException("Apenas agendamentos PENDENTES podem receber desconto");
        }
        this.valor = this.valor.aplicarDesconto(percentual);
    }

    private void addDomainEvent(DomainEvent evento) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(evento);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public ValorMonetario getValor() {
        return valor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", profissionalId=" + profissionalId +
                ", dataAgendamento=" + dataAgendamento +
                ", horario=" + horario +
                ", tipoServico=" + tipoServico +
                ", status=" + status +
                ", valor=" + valor +
                '}';
    }

    public enum TipoServico {
        CONSULTA_VETERINARIA,
        BANHO,
        TOSA,
        VACINACAO,
        CIRURGIA,
        PASSEIO,
        HOSPEDAGEM
    }

    public enum StatusAgendamento {
        PENDENTE,
        CONFIRMADO,
        CANCELADO,
        CONCLUIDO
    }
}