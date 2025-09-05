package com.ivo.trader.bot.services;

import com.ivo.trader.bot.integrations.KrakenAPIService;
import com.ivo.trader.bot.records.CurrencyAmount;
import com.ivo.trader.bot.repositories.CurrencyAmountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private final CurrencyAmountRepository currencyAmountRepository;
    private final KrakenAPIService krakenService;

    public CurrencyService(CurrencyAmountRepository currencyAmountRepository, KrakenAPIService krakenService) {
        this.currencyAmountRepository = currencyAmountRepository;
        this.krakenService = krakenService;
    }

    public BigDecimal getUsdAmount() {
        return currencyAmountRepository.getUsdAmount();
    }

    public BigDecimal getAmountByCurrencyCrypto(String currencyCode) {
        return currencyAmountRepository.getAmountByCurrencyCrypto(currencyCode);
    }

    public List<CurrencyAmount> getAllCrypto() {
        return currencyAmountRepository.getAllCurrencyCrypto();
    }

    public void reset() {
        currencyAmountRepository.resetUsd();
        currencyAmountRepository.resetAllCrypto();
    }

    @Transactional
    public void buyCurrency(String currencyCrypto, BigDecimal amountCrypto) {
        BigDecimal amountUsd = amountCrypto.multiply(krakenService.getBuyPrice(currencyCrypto));

        currencyAmountRepository.decreaseUsdAmount(amountUsd);
        currencyAmountRepository.increaseAmountByCurrencyCrypto(currencyCrypto, amountCrypto);
    }

    @Transactional
    public void sellCurrency(String currencyCrypto, BigDecimal amountCrypto) {
        BigDecimal amountUsd = amountCrypto.multiply(krakenService.getSellPrice(currencyCrypto));

        currencyAmountRepository.decreaseAmountByCurrencyCrypto(currencyCrypto, amountCrypto);
        currencyAmountRepository.increaseUsdAmount(amountUsd);
    }
}
