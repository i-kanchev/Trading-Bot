package com.ivo.trader.bot.records;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record Transaction(
        String currency,
        BigDecimal quantity,
        BigDecimal price,
        String action,
        Timestamp madeAt
) {
}
