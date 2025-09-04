package com.ivo.trader.bot.services;

import com.ivo.trader.bot.records.Transaction;
import com.ivo.trader.bot.repositories.TransactionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;

    public TransactionsService(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionsRepository.getAllTransactions();
    }

    public List<Transaction> getAllTransactionsByCurrency(String currencyCrypto) {
        return transactionsRepository.getAllTransactions().stream()
                .filter(t -> t.currencyFrom().equals(currencyCrypto) || t.currencyTo().equals(currencyCrypto))
                .toList();
    }
}
