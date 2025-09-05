package com.ivo.trader.bot.records;

import java.math.BigDecimal;
import java.util.List;

public record TickerInfo(
        List<BigDecimal> a,
        List<BigDecimal> b,
        List<BigDecimal> c,
        List<BigDecimal> v,
        List<BigDecimal> p,
        List<Integer> t,
        List<BigDecimal> l,
        List<BigDecimal> h,
        BigDecimal o
) {
}
