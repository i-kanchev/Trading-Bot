package com.ivo.trader.bot.services;

import com.ivo.trader.bot.exceptions.InsufficientAmountException;
import com.ivo.trader.bot.integrations.KrakenAPIService;
import com.ivo.trader.bot.repositories.CurrencyAmountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CurrencyService {
    private final CurrencyAmountRepository currencyAmountRepository;
    private final KrakenAPIService krakenService;

    public CurrencyService(CurrencyAmountRepository currencyAmountRepository, KrakenAPIService krakenService) {
        this.currencyAmountRepository = currencyAmountRepository;
        this.krakenService = krakenService;
    }

    @Transactional
    public void buyCurrency(String currencyFiat, String currencyCrypto, BigDecimal amountCrypto) throws InsufficientAmountException {
        BigDecimal amountFiat = amountCrypto.multiply(krakenService.getBuyPrice(currencyCrypto));

        if (currencyAmountRepository.getAmountByCurrencyFiat(currencyFiat).compareTo(amountFiat) <= 0) {
            throw new InsufficientAmountException("Insufficient amount of money");
        }
        currencyAmountRepository.decreaseAmountByCurrencyFiat(currencyFiat, amountFiat);
        currencyAmountRepository.increaseAmountByCurrencyCrypto(currencyCrypto, amountCrypto);
    }

    @Transactional
    public void sellCurrency(String currencyFiat, String currencyCrypto, BigDecimal amountCrypto) throws InsufficientAmountException {
        if (currencyAmountRepository.getAmountByCurrencyCrypto(currencyCrypto).compareTo(amountCrypto) <= 0) {
            throw new InsufficientAmountException("Insufficient amount of money");
        }

        BigDecimal amountFiat = amountCrypto.multiply(krakenService.getSellPrice(currencyCrypto));

        currencyAmountRepository.decreaseAmountByCurrencyCrypto(currencyCrypto, amountCrypto);
        currencyAmountRepository.increaseAmountByCurrencyFiat(currencyFiat, amountFiat);
    }
}
