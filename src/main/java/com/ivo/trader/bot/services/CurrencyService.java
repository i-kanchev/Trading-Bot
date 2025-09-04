package com.ivo.trader.bot.services;

import com.ivo.trader.bot.exceptions.InsufficientAmountException;
import com.ivo.trader.bot.exceptions.InvalidCurrencyException;
import com.ivo.trader.bot.integrations.KrakenAPIService;
import com.ivo.trader.bot.repositories.CurrencyAmountRepository;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public void buyCurrency(String currencyFiat, String currencyCrypto, @org.jetbrains.annotations.NotNull BigDecimal amountCrypto) throws InvalidCurrencyException, InsufficientAmountException {
        BigDecimal amountFiat = amountCrypto.multiply(krakenService.getBuyPrice(currencyCrypto));

        try {
            if (currencyAmountRepository.getAmountByCurrencyFiat(currencyFiat).compareTo(amountFiat) <= 0) {
                throw new InsufficientAmountException("Insufficient amount of money");
            }
        }
        catch (EmptyResultDataAccessException e) {
            throw new InvalidCurrencyException("Invalid currency");
        }
        currencyAmountRepository.decreaseAmountByCurrencyFiat(currencyFiat, amountFiat);
        currencyAmountRepository.increaseAmountByCurrencyCrypto(currencyCrypto, amountCrypto);
    }

    @Transactional
    public void sellCurrency(String currencyFiat, String currencyCrypto, BigDecimal amountCrypto) throws InvalidCurrencyException, InsufficientAmountException {
        try {
            if (currencyAmountRepository.getAmountByCurrencyCrypto(currencyCrypto).compareTo(amountCrypto) <= 0) {
                throw new InsufficientAmountException("Insufficient amount of money");
            }
        }
        catch (EmptyResultDataAccessException e) {
            throw new InvalidCurrencyException("Invalid currency");
        }

        BigDecimal amountFiat = amountCrypto.multiply(krakenService.getSellPrice(currencyCrypto));

        currencyAmountRepository.decreaseAmountByCurrencyCrypto(currencyCrypto, amountCrypto);
        currencyAmountRepository.increaseAmountByCurrencyFiat(currencyFiat, amountFiat);
    }
}
