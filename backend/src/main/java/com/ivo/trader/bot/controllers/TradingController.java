package com.ivo.trader.bot.controllers;

import com.ivo.trader.bot.integrations.KrakenAPIService;
import com.ivo.trader.bot.records.CurrencyAmount;
import com.ivo.trader.bot.records.CurrencyState;
import com.ivo.trader.bot.services.CurrencyService;
import com.ivo.trader.bot.services.ExchangeService;
import com.ivo.trader.bot.services.LiveTradingService;
import com.ivo.trader.bot.services.TrainingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TradingController {
    private final LiveTradingService liveTradingService;
    private final TrainingService trainingService;
    private final ExchangeService exchangeService;
    private final KrakenAPIService krakenService;
    private final CurrencyService currencyService;


    public TradingController(LiveTradingService liveTradingService, TrainingService trainingService,
                             ExchangeService exchangeService, KrakenAPIService krakenService, CurrencyService currencyService) {
        this.liveTradingService = liveTradingService;
        this.trainingService = trainingService;
        this.exchangeService = exchangeService;
        this.krakenService = krakenService;
        this.currencyService = currencyService;
    }

    @PostMapping("/training")
    public BigDecimal train(@RequestParam String crypto, @RequestParam BigDecimal budget, @RequestParam Timestamp start, @RequestParam Timestamp end) {
        int interval = Math.toIntExact(end.getTime() - start.getTime()) / 60;
        return trainingService.train(crypto, budget, interval, start, end);
    }

    @PostMapping("/trading")
    public void trade(@RequestParam String action) {
        switch (action) {
            case "start" -> {
                int interval = 1;
                Timestamp time = Timestamp.from(Instant.now().minus(30, ChronoUnit.MINUTES));
                liveTradingService.trade(interval, time);
            }
            case "reset" -> exchangeService.reset();
        }
    }

    @GetMapping("/trading")
    public List<CurrencyState> getCryptoStatus() {
        return currencyService.getAllCrypto().stream()
                .map(currency -> new CurrencyState(
                        currency.currency(),
                        currency.amount(),
                        krakenService.getSellPrice(currency.currency())
                ))
                .collect(Collectors.toList());
    }
}
