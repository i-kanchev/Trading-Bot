package com.ivo.trader.bot.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ExchangeService {
    private final CurrencyService currencyService;
    private final TransactionsService transactionsService;
    private final PerformanceInfoService performanceInfoService;

    public ExchangeService(CurrencyService currencyService, TransactionsService transactionsService, PerformanceInfoService performanceInfoService) {
        this.currencyService = currencyService;
        this.transactionsService = transactionsService;
        this.performanceInfoService = performanceInfoService;
    }

    @Transactional
    public void buyCrypto(String crypto, BigDecimal quantity, BigDecimal price) {
        currencyService.buyCurrency(crypto, quantity);
        transactionsService.addTransaction(crypto, quantity, price, "buy");
        performanceInfoService.addCurrentRevenue();
    }

    @Transactional
    public void sellCrypto(String crypto, BigDecimal quantity, BigDecimal price) {
        currencyService.sellCurrency(crypto, quantity);
        transactionsService.addTransaction(crypto, quantity, price, "sell");
        performanceInfoService.addCurrentRevenue();
    }
}
