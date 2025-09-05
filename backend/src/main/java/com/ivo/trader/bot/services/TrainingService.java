package com.ivo.trader.bot.services;

import com.ivo.trader.bot.integrations.KrakenAPIService;
import com.ivo.trader.bot.records.ActionTaken;
import com.ivo.trader.bot.records.OHLCCandle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TrainingService {
    private final PredictionService predictionService;
    private final KrakenAPIService krakenService;

    public TrainingService(PredictionService predictionService, KrakenAPIService krakenService) {
        this.predictionService = predictionService;
        this.krakenService = krakenService;
    }

    public BigDecimal train(String cryptoCode, BigDecimal budget, Integer interval, Timestamp start, Timestamp end) {
        BigDecimal usd = budget;
        BigDecimal crypto = BigDecimal.ZERO;

        List<OHLCCandle> candles = krakenService.getCurrencyHistory(cryptoCode, interval, start, end)
                .stream().filter(c -> c.time().before(end))
                .toList();

        for (int i = 0; i < candles.size() - 30; i++) {
            List<OHLCCandle> window = candles.subList(i, i + 30);

            ActionTaken actionToTake = predictionService.takeDecision(window);

            switch (actionToTake.state()) {
                case BUY -> {
                    BigDecimal usdToSell = usd.multiply(BigDecimal.valueOf(actionToTake.percent()));
                    BigDecimal cryptoToBuy = usdToSell.divide(window.getLast().close(), 8, RoundingMode.HALF_UP);

                    usd = usd.subtract(usdToSell);
                    crypto = crypto.add(cryptoToBuy);
                }
                case SELL -> {
                    BigDecimal cryptoToSell = crypto.multiply(BigDecimal.valueOf(actionToTake.percent()));
                    BigDecimal usdToBuy = cryptoToSell.divide(window.getLast().close(), 8, RoundingMode.HALF_UP);

                    crypto = crypto.subtract(cryptoToSell);
                    usd = usd.add(usdToBuy);
                }
            }
        }

        return usd.add(crypto.multiply(candles.getLast().close()));
    }
}
