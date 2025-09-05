package com.ivo.trader.bot.records;

import java.util.List;
import java.util.Map;

public record KrakenResponse (
    String[] error,
    Map<String, List<OHLCCandle>> result
) {
}
