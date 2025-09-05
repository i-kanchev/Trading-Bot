package com.ivo.trader.bot.services;

import com.ivo.trader.bot.enums.ActionState;
import com.ivo.trader.bot.records.ActionTaken;
import com.ivo.trader.bot.records.OHLCCandle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {
    public PredictionService() {}

    public ActionTaken takeDecision(List<OHLCCandle> candles) {
        ActionState action;
        double percent = 0.0;

        double mean = calculateMean(candles);
        double std = calculateStdDev(candles, mean);

        double sma = calculateSMA(candles, candles.size() / 4);
        double atr = calculateATR(candles, candles.size() / 3);
        double rsi = calculateRSI(candles, candles.size() / 3);

        double close = candles.getLast().close().doubleValue();

        double z = Math.round(std * 1000) == 0 ? 0 : (close - mean) / std;
        double vol = atr / mean;

        if (close > sma && rsi < 40 && z < 0.0) {
            action = ActionState.BUY;
            percent = (40 - rsi) * 0.5 + (-z) * 10 - vol * 100;
            percent = Math.max(0, Math.min(50, percent));
        } else if (close < sma || rsi > 60 || z > 0) {
            action = ActionState.SELL;
            percent = (rsi - 60) * 2 + z * 20 + vol * 100;
            percent = Math.max(0, Math.min(100, percent));
        } else {
            action = ActionState.HOLD;
        }

        return new ActionTaken(action, percent);
    }

    private Double calculateMean(List<OHLCCandle> candles) {
        double sum = 0.0;

        for (OHLCCandle c : candles) {
            sum += c.close().doubleValue();
        }

        return sum / candles.size();
    }

    private Double calculateStdDev(List<OHLCCandle> candles, Double mean) {
        double sumSq = 0.0;

        for (OHLCCandle c : candles) {
            double diff = c.close().doubleValue() - mean;
            sumSq += diff * diff;
        }

        double variance = sumSq / candles.size();
        return Math.sqrt(variance);
    }

    // SMA (Simple Moving Average)
    private Double calculateSMA(List<OHLCCandle> candles, Integer n) {
        double sum = 0.0;

        for (int i = candles.size() - n; i < candles.size(); i++) {
            sum += candles.get(i).close().doubleValue();
        }

        return sum / n;
    }

    // ATR (Average True Range)
    private Double calculateATR(List<OHLCCandle> candles, Integer n) {
        double sum = 0.0;

        for (int i = candles.size() - n; i < candles.size(); i++) {
            OHLCCandle c = candles.get(i);
            OHLCCandle prev = candles.get(i - 1);
            double tr = Math.max(c.high().doubleValue() - c.low().doubleValue(),
                                Math.max(Math.abs(c.high().doubleValue() - prev.close().doubleValue()),
                                        Math.abs(c.low().doubleValue() - prev.close().doubleValue())));
            sum += tr;
        }

        return sum / n;
    }

    // RSI (Relative Strength Index)
    private Double calculateRSI(List<OHLCCandle> candles, Integer n) {
        double gainSum = 0.0;
        double lossSum = 0.0;

        for (int i = candles.size() - n; i < candles.size(); i++) {
            double change = candles.get(i).close().doubleValue() - candles.get(i - 1).close().doubleValue();
            if (change > 0) {
                gainSum += change;
            }
            else {
                lossSum -= change;
            }
        }

        if (lossSum == 0) {
            return 100.0;
        }

        double rs = gainSum / lossSum;
        return 100.0 - (100.0 / (1 + rs));
    }
}
