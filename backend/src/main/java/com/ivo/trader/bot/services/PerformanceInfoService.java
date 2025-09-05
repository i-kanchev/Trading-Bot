package com.ivo.trader.bot.services;

import com.ivo.trader.bot.integrations.KrakenAPIService;
import com.ivo.trader.bot.records.CurrencyAmount;
import com.ivo.trader.bot.records.PerformanceInfo;
import com.ivo.trader.bot.repositories.CurrencyAmountRepository;
import com.ivo.trader.bot.repositories.PerformanceInfoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PerformanceInfoService {
    private final PerformanceInfoRepository performanceInfoRepository;
    private final CurrencyAmountRepository currencyAmountRepository;
    private final KrakenAPIService krakenService;

    public PerformanceInfoService(PerformanceInfoRepository performanceInfoRepository, CurrencyAmountRepository currencyAmountRepository, KrakenAPIService krakenService) {
        this.performanceInfoRepository = performanceInfoRepository;
        this.currencyAmountRepository = currencyAmountRepository;
        this.krakenService = krakenService;
    }

    public List<PerformanceInfo> getPerformanceInfo() {
        return performanceInfoRepository.getAllPerformanceInfo();
    }

    public void addCurrentRevenue() {
        BigDecimal currRevenue = currencyAmountRepository.getUsdAmount();

        for (CurrencyAmount crypto : currencyAmountRepository.getAllCurrencyCrypto()) {
            BigDecimal cryptoToFiat = crypto.amount();
            cryptoToFiat = cryptoToFiat.multiply(krakenService.getSellPrice(crypto.currency()));

            currRevenue = currRevenue.add(cryptoToFiat);
        }

        performanceInfoRepository.addEntry(currRevenue);
    }

    public void reset() {
        performanceInfoRepository.reset();
    }
}
