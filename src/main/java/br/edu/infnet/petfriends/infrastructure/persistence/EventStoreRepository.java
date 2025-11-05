package br.edu.infnet.petfriends.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventStoreRepository extends JpaRepository<StoredEvent, String> {

    @Query("SELECT e FROM StoredEvent e WHERE e.agregadoId = :agregadoId ORDER BY e.ocorridoEm ASC")
    List<StoredEvent> findByAgregadoIdOrderByOcorridoEmAsc(@Param("agregadoId") Long agregadoId);

    List<StoredEvent> findByTipoEventoOrderByOcorridoEmDesc(String tipoEvento);

    @Query("SELECT e FROM StoredEvent e WHERE e.ocorridoEm BETWEEN :inicio AND :fim ORDER BY e.ocorridoEm ASC")
    List<StoredEvent> findByPeriodo(@Param("inicio") LocalDateTime inicio,
                                    @Param("fim") LocalDateTime fim);

    @Query("SELECT e FROM StoredEvent e WHERE e.agregadoId = :agregadoId AND e.ocorridoEm <= :ate ORDER BY e.ocorridoEm ASC")
    List<StoredEvent> findByAgregadoIdUntil(@Param("agregadoId") Long agregadoId,
                                            @Param("ate") LocalDateTime ate);

    @Query("SELECT COUNT(e) FROM StoredEvent e WHERE e.agregadoId = :agregadoId")
    Long countByAgregadoId(@Param("agregadoId") Long agregadoId);

    List<StoredEvent> findByTipoAgregadoOrderByOcorridoEmDesc(String tipoAgregado);
}