package com.ivo.trader.bot.records;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record CurrencyPriceHistory(
        String currency,
        BigDecimal priceBuy,
        BigDecimal priceSell,
        Timestamp time
) {}
