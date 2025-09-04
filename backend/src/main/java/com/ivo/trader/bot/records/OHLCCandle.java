package com.ivo.trader.bot.records;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record OHLCCandle(
    Timestamp time,
    BigDecimal open,
    BigDecimal high,
    BigDecimal low,
    BigDecimal close,
    BigDecimal vwap,
    BigDecimal volume,
    Integer trades
) {
}
