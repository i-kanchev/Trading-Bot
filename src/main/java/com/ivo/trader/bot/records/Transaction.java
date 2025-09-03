package com.ivo.trader.bot.records;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record Transaction(
        String currencyFrom,
        BigDecimal quantityFrom,
        BigDecimal priceFrom,
        String currencyTo,
        BigDecimal quantityTo,
        BigDecimal priceTo,
        Timestamp madeAt
) {}
