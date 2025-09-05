package com.ivo.trader.bot.controllers;

import com.ivo.trader.bot.services.LiveTradingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class TradingController {
    private final LiveTradingService liveTradingService;

    public TradingController(LiveTradingService liveTradingService) {
        this.liveTradingService = liveTradingService;
    }

    @PostMapping("/trading/live")
    public void trade(@RequestParam Integer interval, @RequestParam Timestamp timestamp) {
        liveTradingService.trade(interval, timestamp);
    }
}
