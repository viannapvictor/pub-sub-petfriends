package br.edu.infnet.petfriends.valueobject;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

public class Horario implements Serializable {

    private static final long serialVersionUID = 1L;
    private final LocalTime hora;

    public Horario(LocalTime hora) {
        if (hora == null) {
            throw new IllegalArgumentException("Horário não pode ser nulo");
        }

        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new IllegalArgumentException("Horário deve estar entre 08:00 e 18:00");
        }

        if (hora.getMinute() % 30 != 0 || hora.getSecond() != 0) {
            throw new IllegalArgumentException("Horário deve ser em intervalos de 30 minutos (ex: 09:00, 09:30)");
        }

        this.hora = hora;
    }

    public LocalTime getHora() {
        return hora;
    }

    public boolean ehManha() {
        return hora.isBefore(LocalTime.NOON);
    }

    public boolean ehTarde() {
        return !hora.isBefore(LocalTime.NOON);
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        Horario outroHorario = (Horario) objeto;
        return Objects.equals(hora, outroHorario.hora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hora);
    }

    @Override
    public String toString() {
        return hora.toString();
    }
}
