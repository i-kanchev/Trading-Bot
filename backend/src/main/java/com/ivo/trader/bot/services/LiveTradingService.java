package com.ivo.trader.bot.services;

import com.ivo.trader.bot.integrations.KrakenAPIService;
import com.ivo.trader.bot.records.ActionTaken;
import com.ivo.trader.bot.records.CurrencyAmount;
import com.ivo.trader.bot.repositories.CurrencyAmountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

@Service
public class LiveTradingService {
    private final CurrencyAmountRepository currencyAmountRepository;
    private final PredictionService predictionService;
    private final ExchangeService exchangeService;
    private final KrakenAPIService krakenService;

    public LiveTradingService(CurrencyAmountRepository currencyAmountRepository, PredictionService predictionService,
                              ExchangeService exchangeService, KrakenAPIService krakenService) {
        this.currencyAmountRepository = currencyAmountRepository;
        this.predictionService = predictionService;
        this.exchangeService = exchangeService;
        this.krakenService = krakenService;
    }

    @Transactional
    public void trade(Integer interval, Timestamp timestamp) {
        List<CurrencyAmount> currenciesCrypto= currencyAmountRepository.getAllCurrencyCrypto();

        for (CurrencyAmount currCrypto : currenciesCrypto) {
            ActionTaken actionToTake = predictionService.takeDecision(krakenService.getCurrencyHistory(currCrypto.currency(), interval, timestamp));

            switch (actionToTake.state()) {
                case BUY -> {
                    BigDecimal amountUsd = currencyAmountRepository.getUsdAmount();
                    BigDecimal buyPrice = krakenService.getBuyPrice(currCrypto.currency());

                    exchangeService.buyCrypto(
                        currCrypto.currency(),
                        amountUsd.divide(buyPrice, 8, RoundingMode.HALF_UP),
                        krakenService.getBuyPrice(currCrypto.currency())
                    );
                }
                case SELL -> exchangeService.sellCrypto(
                    currCrypto.currency(),
                    currCrypto.amount().multiply(BigDecimal.valueOf(actionToTake.percent())),
                    krakenService.getSellPrice(currCrypto.currency())
                );
            }
        }
    }
}
