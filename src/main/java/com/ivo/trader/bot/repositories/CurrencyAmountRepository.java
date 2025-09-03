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

    public List<CurrencyAmount> getAllCurrencyFiat() {
        String sql = "SELECT * FROM fiat_currency";
        return jdbcTemplate.query(sql, new CurrencyAmountRepository.CurrencyAmountRowMapper());
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

    public List<CurrencyAmount> getAllCurrencyCrypto() {
        String sql = "SELECT * FROM crypto_currency";
        return jdbcTemplate.query(sql, new CurrencyAmountRepository.CurrencyAmountRowMapper());
    }
}
