package com.ivo.trader.bot.integrations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.trader.bot.records.KrakenResponse;
import com.ivo.trader.bot.records.KrakenTickerResponse;
import com.ivo.trader.bot.records.OHLCCandle;
import com.ivo.trader.bot.records.TickerInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class KrakenAPIService {
    private static final String API_URL_TICKER = "https://api.kraken.com/0/public/Ticker?pair=";
    private static final String API_URL_OHLC = "https://api.kraken.com/0/public/OHLC?pair=";
    private static final String API_URL_USD = "USD";
    private static final String API_URL_INTERVAL = "&interval=";
    private static final String API_URL_SINCE = "&since=";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getCryptoTicker(String currency) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL_TICKER + currency + API_URL_USD))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch crypto ticker from Kraken", e);
        }
    }

    public String getCryptoOHLCData(String currency, Integer interval, Timestamp since) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL_OHLC + currency + API_URL_USD
                            + API_URL_INTERVAL + interval.toString()
                            + API_URL_SINCE + since.toString()))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch crypto history from Kraken", e);
        }
    }

    /**
     * Returns the best ask (price you pay to buy).
     */
    public BigDecimal getBuyPrice(String currency) {
        String json = getCryptoTicker(currency);

        ObjectMapper mapper = new ObjectMapper();
        KrakenTickerResponse krakenResponse;

        try {
            krakenResponse = mapper.readValue(json, KrakenTickerResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String onlyKey = krakenResponse.result().keySet().iterator().next();
        return krakenResponse.result().get(onlyKey).b().getFirst();
    }

    /**
     * Returns the best bid (price you get if you sell).
     */
    public BigDecimal getSellPrice(String currency) {
        String json = getCryptoTicker(currency);

        ObjectMapper mapper = new ObjectMapper();
        KrakenTickerResponse krakenResponse;

        try {
            krakenResponse = mapper.readValue(json, KrakenTickerResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String onlyKey = krakenResponse.result().keySet().iterator().next();
        return krakenResponse.result().get(onlyKey).a().getFirst();
    }

    public List<OHLCCandle> getCurrencyHistory(String currency, Integer interval, Timestamp since) {
        String json = getCryptoOHLCData(currency, interval, since);

        ObjectMapper mapper = new ObjectMapper();
        KrakenResponse krakenResponse;
        try {
            krakenResponse = mapper.readValue(json, KrakenResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return krakenResponse.result().values().stream().findFirst().orElse(List.of());
    }

    public List<OHLCCandle> getCurrencyHistory(String currency, Integer interval, Timestamp start, Timestamp end) {
        String json = getCryptoOHLCData(currency, interval, start);

        ObjectMapper mapper = new ObjectMapper();
        KrakenResponse krakenResponse;
        try {
            krakenResponse = mapper.readValue(json, KrakenResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List<OHLCCandle> candles = krakenResponse.result().values().stream().findFirst().orElse(List.of());

        return candles.stream().filter(c -> c.time().before(end)).toList();
    }
}
