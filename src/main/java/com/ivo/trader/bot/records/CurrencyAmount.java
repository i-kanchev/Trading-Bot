package com.ivo.trader.bot.records;

import java.math.BigDecimal;

public record CurrencyAmount(
        String currency,
        BigDecimal amount
) {
}
