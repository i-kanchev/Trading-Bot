package com.ivo.trader.bot.services;

import com.ivo.trader.bot.exceptions.InsufficientAmountException;
import com.ivo.trader.bot.records.ActionTaken;
import com.ivo.trader.bot.records.CurrencyAmount;
import com.ivo.trader.bot.repositories.CurrencyAmountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class LiveTradingService {
    private final CurrencyAmountRepository currencyAmountRepository;
    private final PredictionService predictionService;
    private final CurrencyService currencyService;

    public LiveTradingService(CurrencyAmountRepository currencyAmountRepository, PredictionService predictionService, CurrencyService currencyService) {
        this.currencyAmountRepository = currencyAmountRepository;
        this.predictionService = predictionService;
        this.currencyService = currencyService;
    }

    @Transactional
    public void trade(Integer interval, Timestamp timestamp) throws InsufficientAmountException {
        BigDecimal amountFiat = currencyAmountRepository.getAmountByCurrencyFiat(CurrencyAmountRepository.MAIN_FIAT_CURRENCY);
        List<CurrencyAmount> currenciesCrypto= currencyAmountRepository.getAllCurrencyCrypto();

        for (CurrencyAmount currCrypto : currenciesCrypto) {
            ActionTaken actionToTake = predictionService.takeDecision(currCrypto.currency(), interval, timestamp);

            switch (actionToTake.state()) {
                case BUY -> {
                    BigDecimal amountToBuy = amountFiat.multiply(BigDecimal.valueOf(actionToTake.percent()));
                    currencyService.buyCurrency(CurrencyAmountRepository.MAIN_FIAT_CURRENCY, currCrypto.currency(), amountToBuy);
                    amountFiat = amountFiat.subtract(amountToBuy);
                }
                case SELL -> {
                    BigDecimal amountToSell = currCrypto.amount().multiply(BigDecimal.valueOf(actionToTake.percent()));
                    currencyService.sellCurrency(CurrencyAmountRepository.MAIN_FIAT_CURRENCY, currCrypto.currency(), amountToSell);
                    amountFiat = currencyAmountRepository.getAmountByCurrencyFiat(CurrencyAmountRepository.MAIN_FIAT_CURRENCY);
                }
            }
        }
    }
}
