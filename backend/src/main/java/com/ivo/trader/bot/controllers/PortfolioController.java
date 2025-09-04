package com.ivo.trader.bot.controllers;

import com.ivo.trader.bot.records.PerformanceInfo;
import com.ivo.trader.bot.records.Transaction;
import com.ivo.trader.bot.services.PerformanceInfoService;
import com.ivo.trader.bot.services.TransactionsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PortfolioController {
    private final TransactionsService transactionsService;
    private final PerformanceInfoService performanceInfoService;

    public PortfolioController(TransactionsService transactionsService, PerformanceInfoService performanceInfoService) {
        this.transactionsService = transactionsService;
        this.performanceInfoService = performanceInfoService;
    }

    @GetMapping("/portfolio/performance")
    public List<PerformanceInfo> getPerformance() {
        return performanceInfoService.getPerformanceInfo();
    }

    @PostMapping("/portfolio/performance")
    public void addCurrentRevenue() {
        performanceInfoService.addCurrentRevenue();
    }

    @GetMapping("/portfolio/transactions")
    public List<Transaction> getAllTransactions(@RequestParam(required = false) String currency) {
        if (currency == null) {
            return transactionsService.getAllTransactions();
        } else {
            return transactionsService.getAllTransactionsByCurrency(currency);
        }
    }
}
