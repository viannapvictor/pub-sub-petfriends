package br.edu.infnet.petfriends.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class ValorMonetario implements Serializable {

    private static final long serialVersionUID = 1L;
    private final BigDecimal quantia;

    public ValorMonetario(BigDecimal quantia) {
        if (quantia == null) {
            throw new IllegalArgumentException("Valor monetário não pode ser nulo");
        }
        if (quantia.signum() < 0) {
            throw new IllegalArgumentException("Valor monetário não pode ser negativo");
        }
        this.quantia = quantia.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getQuantia() {
        return this.quantia;
    }

    public ValorMonetario somar(ValorMonetario outro) {
        if (outro == null) {
            throw new IllegalArgumentException("Outro valor não pode ser nulo");
        }
        return new ValorMonetario(this.quantia.add(outro.getQuantia()));
    }

    public ValorMonetario subtrair(ValorMonetario outro) {
        if (outro == null) {
            throw new IllegalArgumentException("Outro valor não pode ser nulo");
        }
        BigDecimal resultado = this.quantia.subtract(outro.getQuantia());
        if (resultado.signum() < 0) {
            throw new IllegalArgumentException("Resultado da subtração não pode ser negativo");
        }
        return new ValorMonetario(resultado);
    }

    public ValorMonetario aplicarDesconto(BigDecimal percentual) {
        if (percentual == null || percentual.signum() < 0 || percentual.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentual deve estar entre 0 e 100");
        }
        BigDecimal desconto = this.quantia.multiply(percentual).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return new ValorMonetario(this.quantia.subtract(desconto));
    }

    public boolean isZero() {
        return quantia.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) return true;
        if (objeto == null || getClass() != objeto.getClass()) return false;
        ValorMonetario outro = (ValorMonetario) objeto;
        return Objects.equals(this.quantia, outro.quantia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantia);
    }

    @Override
    public String toString() {
        return "R$ " + quantia.toString();
    }
}