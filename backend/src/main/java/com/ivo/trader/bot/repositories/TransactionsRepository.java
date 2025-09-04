package com.ivo.trader.bot.repositories;

import com.ivo.trader.bot.records.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
                    rs.getString("currency_from"),
                    rs.getBigDecimal("quantity_from"),
                    rs.getBigDecimal("price_from"),
                    rs.getString("currency_to"),
                    rs.getBigDecimal("quantity_to"),
                    rs.getBigDecimal("price_to"),
                    rs.getTimestamp("made_at")
            );
        }
    }

    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions";
        return jdbcTemplate.query(sql, new TransactionRowMapper());
    }
}
