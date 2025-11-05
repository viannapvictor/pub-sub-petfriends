package br.edu.infnet.petfriends.infrastructure.converters;

import br.edu.infnet.petfriends.valueobject.ValorMonetario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class ValorMonetarioConverter implements AttributeConverter<ValorMonetario, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(ValorMonetario valorMonetario) {
        if (valorMonetario == null) {
            return null;
        }
        return valorMonetario.getQuantia();
    }

    @Override
    public ValorMonetario convertToEntityAttribute(BigDecimal quantia) {
        if (quantia == null) {
            return null;
        }
        return new ValorMonetario(quantia);
    }
}