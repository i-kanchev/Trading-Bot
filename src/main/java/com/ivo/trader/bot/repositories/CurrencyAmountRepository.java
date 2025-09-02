package com.ivo.trader.bot.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class CurrencyAmountRepository {
    private final JdbcTemplate jdbcTemplate;

    public CurrencyAmountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BigDecimal getAmountByCurrencyFiat(String currencyCode) {
        String sql = "SELECT amount FROM fiat_currency WHERE currency = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, currencyCode);
    }

    public Integer increaseAmountByCurrencyFiat(String currencyCode, BigDecimal amount) {
        String sql = "UPDATE fiat_currency SET amount = amount + ? WHERE currency = ?";
        return jdbcTemplate.update(sql, amount, currencyCode);
    }

    public Integer decreaseAmountByCurrencyFiat(String currencyCode, BigDecimal amount) {
        String sql = "UPDATE fiat_currency SET amount = amount - ? WHERE currency = ?";
        return jdbcTemplate.update(sql, amount, currencyCode);
    }

    public List<String> getAllCurrencyCodesFiat() {
        String sql = "SELECT currency FROM fiat_currency";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public BigDecimal getAmountByCurrencyCrypto(String currencyCode) {
        String sql = "SELECT amount FROM crypto_currency WHERE currency = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, currencyCode);
    }

    public Integer increaseAmountByCurrencyCrypto(String currencyCode, BigDecimal amount) {
        String sql = "UPDATE crypto_currency SET amount = amount + ? WHERE currency = ?";
        return jdbcTemplate.update(sql, amount, currencyCode);
    }

    public Integer decreaseAmountByCurrencyCrypto(String currencyCode, BigDecimal amount) {
        String sql = "UPDATE crypto_currency SET amount = amount - ? WHERE currency = ?";
        return jdbcTemplate.update(sql, amount, currencyCode);
    }

    public List<String> getAllCurrencyCodesCrypto() {
        String sql = "SELECT currency FROM crypto_currency";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
