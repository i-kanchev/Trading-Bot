package com.ivo.trader.bot.services;

import com.ivo.trader.bot.records.Transaction;
import com.ivo.trader.bot.repositories.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;

    public TransactionsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public void addTransaction(String crypto, BigDecimal quantity, BigDecimal price, String action) {
        transactionsRepository.addTransaction(crypto, quantity, price, action);
    }

    public List<Transaction> getAllTransactions() {
        return transactionsRepository.getAllTransactions();
    }

    public List<Transaction> getAllTransactionsByCurrency(String currencyCrypto) {
        return transactionsRepository.getAllTransactions().stream()
                .filter(t -> t.currency().equals(currencyCrypto))
                .toList();
    }

    public void resetTransactions() {
        transactionsRepository.reset();
    }
}
