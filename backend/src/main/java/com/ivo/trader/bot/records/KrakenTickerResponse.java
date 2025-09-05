package com.ivo.trader.bot.records;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record KrakenTickerResponse (
    String[] error,
    Map<String, List<BigDecimal>> result
) {
}
