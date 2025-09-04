package com.ivo.trader.bot.records;

import com.ivo.trader.bot.enums.ActionState;

public record ActionTaken(
        ActionState state,
        Double percent
) {
}
