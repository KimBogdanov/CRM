package ru.crm.system.converter;

import javax.persistence.AttributeConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyConverter implements AttributeConverter<BigDecimal, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(BigDecimal attribute) {
        return attribute.setScale(2, RoundingMode.UNNECESSARY);
    }

    @Override
    public BigDecimal convertToEntityAttribute(BigDecimal dbData) {
        return dbData.setScale(2, RoundingMode.UNNECESSARY);
    }
}