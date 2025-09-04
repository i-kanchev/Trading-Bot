package com.ivo.trader.bot.repositories;

import com.ivo.trader.bot.records.PerformanceInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PerformanceInfoRepository {
    private final JdbcTemplate jdbcTemplate;

    public PerformanceInfoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class PerformanceInfoRowMapper implements RowMapper<PerformanceInfo> {
        @Override
        public PerformanceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PerformanceInfo(
                    rs.getTimestamp("checked_at"),
                    rs.getBigDecimal("revenue")
            );
        }
    }

    public List<PerformanceInfo> getAllPerformanceInfo() {
        String sql = "SELECT * FROM performance_info";
        return jdbcTemplate.query(sql, new PerformanceInfoRepository.PerformanceInfoRowMapper());
    }

    public Integer addEntry(BigDecimal currentRevenue) {
        String sql = "INSERT INTO performance_info (revenue) VALUES (?)";
        return jdbcTemplate.update(sql, currentRevenue);
    }
}
