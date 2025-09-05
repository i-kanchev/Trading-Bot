package com.ivo.trader.bot.repositories;

import com.ivo.trader.bot.records.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionsRepository {
    private final JdbcTemplate jdbcTemplate;

    public TransactionsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class TransactionRowMapper implements RowMapper<Transaction> {
        @Override
        public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Transaction(
                    rs.getString("currency"),
                    rs.getBigDecimal("quantity"),
                    rs.getBigDecimal("price"),
                    rs.getString("action"),
                    rs.getTimestamp("made_at")
            );
        }
    }

    public void addTransaction(String crypto, BigDecimal quantity, BigDecimal price, String action) {
        String sql = "INSERT INTO transactions (currency, quantity, price, action) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, crypto, quantity, price, action);
    }

    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions";
        return jdbcTemplate.query(sql, new TransactionRowMapper());
    }

    public void reset() {
        String sql = "TRUNCATE TABLE transactions";
        jdbcTemplate.update(sql);
    }
}
