package com.ivo.trader.bot.integrations;

import com.ivo.trader.bot.records.OHLCCandle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

    public String getCryptoOHLCData(String currency, Integer interval, Timestamp since) {
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

    public List<OHLCCandle> getCurrencyHistory(String currency, Integer interval, Timestamp since) {
        try {
            String json = getCryptoOHLCData(currency, interval, since);

            List<OHLCCandle> candles = new ArrayList<>();

            // Find the start of the array for the first currency pair
            int start = json.indexOf('[' , json.indexOf('[') + 1);
            int end = json.indexOf("],\"last\"");
            String arrayContent = json.substring(start, end);

            // Split into individual candle arrays
            String[] candleStrings = arrayContent.split("\\],\\[");

            for (String candleStr : candleStrings) {
                // Clean up brackets
                candleStr = candleStr.replace("[","").replace("]","");
                String[] values = candleStr.split(",");

                OHLCCandle candle = new OHLCCandle(
                        new Timestamp(Long.parseLong(values[0].trim())),
                        new BigDecimal(values[1].replace("\"","").trim()),
                        new BigDecimal(values[2].replace("\"","").trim()),
                        new BigDecimal(values[3].replace("\"","").trim()),
                        new BigDecimal(values[4].replace("\"","").trim()),
                        new BigDecimal(values[5].replace("\"","").trim()),
                        new BigDecimal(values[6].replace("\"","").trim()),
                        Integer.parseInt(values[7].trim())
                );

                candles.add(candle);
            }

            return candles;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OHLC data", e);
        }
    }

    public List<OHLCCandle> getCurrencyHistory(String currency, Integer interval, Timestamp start, Timestamp end) {
        String json = getCryptoOHLCData(currency, interval, start);

        List<OHLCCandle> candles = new ArrayList<>();

        return candles;
    }
}
