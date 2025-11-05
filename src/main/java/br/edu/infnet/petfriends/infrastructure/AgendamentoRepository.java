package br.edu.infnet.petfriends.infrastructure;

import br.edu.infnet.petfriends.domain.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByClienteId(Long clienteId);

    List<Agendamento> findByProfissionalId(Long profissionalId);

    List<Agendamento> findByDataAgendamento(LocalDate data);

    List<Agendamento> findByStatus(Agendamento.StatusAgendamento status);

    @Query("SELECT a FROM Agendamento a WHERE a.profissionalId = :profissionalId " +
            "AND a.dataAgendamento = :data " +
            "AND a.status != 'CANCELADO'")
    List<Agendamento> findByProfissionalIdAndData(
            @Param("profissionalId") Long profissionalId,
            @Param("data") LocalDate data
    );
}