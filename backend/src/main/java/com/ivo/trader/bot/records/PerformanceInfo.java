package com.ivo.trader.bot.records;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record PerformanceInfo(
        Timestamp checkedAt,
        BigDecimal revenue
) {
}
