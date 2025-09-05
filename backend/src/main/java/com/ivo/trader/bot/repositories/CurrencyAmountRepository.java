package com.ivo.trader.bot.repositories;

import com.ivo.trader.bot.records.CurrencyAmount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CurrencyAmountRepository {
    private final JdbcTemplate jdbcTemplate;

    public static final String MAIN_FIAT_CURRENCY = "USD";
    public static final BigDecimal USD_RESET_VALUE = BigDecimal.valueOf(25000);

    public CurrencyAmountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class CurrencyAmountRowMapper implements RowMapper<CurrencyAmount> {
        @Override
        public CurrencyAmount mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CurrencyAmount(
                    rs.getString("currency"),
                    rs.getBigDecimal("amount")
            );
        }
    }

    public BigDecimal getUsdAmount() {
        String sql = "SELECT amount FROM fiat_currency WHERE currency = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, MAIN_FIAT_CURRENCY);
    }

    public void increaseUsdAmount(BigDecimal amount) {
        String sql = "UPDATE fiat_currency SET amount = amount + ? WHERE currency = ?";
        jdbcTemplate.update(sql, amount, MAIN_FIAT_CURRENCY);
    }

    public void decreaseUsdAmount(BigDecimal amount) {
        String sql = "UPDATE fiat_currency SET amount = amount - ? WHERE currency = ?";
        jdbcTemplate.update(sql, amount, MAIN_FIAT_CURRENCY);
    }

    public void resetUsd() {
        String sql = "UPDATE fiat_currency SET amount = ? WHERE currency = ?";
        jdbcTemplate.update(sql, USD_RESET_VALUE, MAIN_FIAT_CURRENCY);
    }

    public BigDecimal getAmountByCurrencyCrypto(String currencyCode) {
        String sql = "SELECT amount FROM crypto_currency WHERE currency = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, currencyCode);
    }

    public void increaseAmountByCurrencyCrypto(String currencyCode, BigDecimal amount) {
        String sql = "UPDATE crypto_currency SET amount = amount + ? WHERE currency = ?";
        jdbcTemplate.update(sql, amount, currencyCode);
    }

    public void decreaseAmountByCurrencyCrypto(String currencyCode, BigDecimal amount) {
        String sql = "UPDATE crypto_currency SET amount = amount - ? WHERE currency = ?";
        jdbcTemplate.update(sql, amount, currencyCode);
    }

    public List<CurrencyAmount> getAllCurrencyCrypto() {
        String sql = "SELECT * FROM crypto_currency";
        return jdbcTemplate.query(sql, new CurrencyAmountRepository.CurrencyAmountRowMapper());
    }

    public void resetAllCrypto() {
        String sql = "UPDATE crypto_currency SET amount = 0";
        jdbcTemplate.update(sql);
    }
}
