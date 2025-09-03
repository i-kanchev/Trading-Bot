package com.ivo.trader.bot.repositories;

import com.ivo.trader.bot.records.CurrencyPriceHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CurrencyPriceHistoryRepository {
    private final JdbcTemplate jdbcTemplate;

    public CurrencyPriceHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class CurrencyPriceRowMapper implements RowMapper<CurrencyPriceHistory> {
        @Override
        public CurrencyPriceHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CurrencyPriceHistory(
                    rs.getString("currency"),
                    rs.getBigDecimal("price_buy"),
                    rs.getBigDecimal("price_sell"),
                    rs.getTimestamp("time")
            );
        }
    }

    public Map<String, CurrencyPriceHistory> getAllCurrencyPricesMappedByCurrency() {
        String sql = "SELECT * FROM currency_history";
        List<CurrencyPriceHistory> prices = jdbcTemplate.query(sql, new CurrencyPriceRowMapper());
        return prices.stream().collect(Collectors.toMap(CurrencyPriceHistory::currency, cph -> cph));
    }
}
