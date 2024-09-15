package com.example.banking.bankingTransactions.repository;

import com.example.banking.bankingTransactions.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

}
