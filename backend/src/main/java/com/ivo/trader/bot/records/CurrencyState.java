package com.ivo.trader.bot.records;

import java.math.BigDecimal;

public record CurrencyState (
        String currency,
        BigDecimal amount,
        BigDecimal price
) {
}
