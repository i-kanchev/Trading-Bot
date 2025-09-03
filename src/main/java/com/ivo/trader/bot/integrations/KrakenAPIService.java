package com.ivo.trader.bot.integrations;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;

@Service
public class KrakenAPIService {
    private static final String API_URL_OPENING = "https://api.kraken.com/0/public/Ticker?pair=";
    private static final String API_URL_USD = "USD";
    private static final String API_URL_INTERVAL = "&interval=";
    private static final String API_URL_SINCE = "&since=";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String getCryptoTicker(String currency) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL_OPENING + currency + API_URL_USD))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch crypto ticker from Kraken", e);
        }
    }

    public String getCryptoHistory(String currency, Integer interval, Timestamp since) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL_OPENING + currency + API_URL_USD
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
        try {
            String marker = "\"a\":[\"";
            int start = json.indexOf(marker) + marker.length();
            int end = json.indexOf("\"", start);
            String price = json.substring(start, end);
            return new BigDecimal(price);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse buy price", e);
        }
    }

    /**
     * Returns the best bid (price you get if you sell).
     */
    public BigDecimal getSellPrice(String currency) {
        String json = getCryptoTicker(currency);
        try {
            String marker = "\"b\":[\"";
            int start = json.indexOf(marker) + marker.length();
            int end = json.indexOf("\"", start);
            String price = json.substring(start, end);
            return new BigDecimal(price);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse sell price", e);
        }
    }
}
