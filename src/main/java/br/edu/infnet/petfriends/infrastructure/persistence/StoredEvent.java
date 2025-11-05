package br.edu.infnet.petfriends.infrastructure.persistence;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "EVENT_STORE", catalog = "PETFRIENDS_DB", schema = "PUBLIC",
        indexes = {
                @Index(name = "idx_agregado_id", columnList = "AGREGADO_ID"),
                @Index(name = "idx_tipo_evento", columnList = "TIPO_EVENTO"),
                @Index(name = "idx_ocorrido_em", columnList = "OCORRIDO_EM")
        })
public class StoredEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "EVENT_ID", nullable = false, length = 36)
    private String eventId;

    @Column(name = "TIPO_EVENTO", nullable = false, length = 100)
    private String tipoEvento;

    @Column(name = "AGREGADO_ID", nullable = false)
    private Long agregadoId;

    @Column(name = "TIPO_AGREGADO", nullable = false, length = 50)
    private String tipoAgregado;

    @Column(name = "OCORRIDO_EM", nullable = false)
    private LocalDateTime ocorridoEm;

    @Column(name = "PERSISTIDO_EM", nullable = false)
    private LocalDateTime persistidoEm;

    @Column(name = "EVENT_DATA", nullable = false, columnDefinition = "TEXT")
    private String eventData;

    @Column(name = "USUARIO", length = 100)
    private String usuario;

    @Column(name = "VERSAO_SCHEMA")
    private Integer versaoSchema;

    public StoredEvent() {
        this.persistidoEm = LocalDateTime.now();
        this.versaoSchema = 1;
    }

    public StoredEvent(String eventId, String tipoEvento, Long agregadoId,
                       String tipoAgregado, LocalDateTime ocorridoEm,
                       String eventData, String usuario) {
        this();
        this.eventId = eventId;
        this.tipoEvento = tipoEvento;
        this.agregadoId = agregadoId;
        this.tipoAgregado = tipoAgregado;
        this.ocorridoEm = ocorridoEm;
        this.eventData = eventData;
        this.usuario = usuario;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Long getAgregadoId() {
        return agregadoId;
    }

    public void setAgregadoId(Long agregadoId) {
        this.agregadoId = agregadoId;
    }

    public String getTipoAgregado() {
        return tipoAgregado;
    }

    public void setTipoAgregado(String tipoAgregado) {
        this.tipoAgregado = tipoAgregado;
    }

    public LocalDateTime getOcorridoEm() {
        return ocorridoEm;
    }

    public void setOcorridoEm(LocalDateTime ocorridoEm) {
        this.ocorridoEm = ocorridoEm;
    }

    public LocalDateTime getPersistidoEm() {
        return persistidoEm;
    }

    public void setPersistidoEm(LocalDateTime persistidoEm) {
        this.persistidoEm = persistidoEm;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getVersaoSchema() {
        return versaoSchema;
    }

    public void setVersaoSchema(Integer versaoSchema) {
        this.versaoSchema = versaoSchema;
    }

    @Override
    public String toString() {
        return "StoredEvent{" +
                "eventId='" + eventId + '\'' +
                ", tipoEvento='" + tipoEvento + '\'' +
                ", agregadoId=" + agregadoId +
                ", tipoAgregado='" + tipoAgregado + '\'' +
                ", ocorridoEm=" + ocorridoEm +
                ", persistidoEm=" + persistidoEm +
                ", usuario='" + usuario + '\'' +
                ", versaoSchema=" + versaoSchema +
                '}';
    }
}