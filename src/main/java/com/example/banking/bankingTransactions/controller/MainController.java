package com.example.banking.bankingTransactions.controller;

import com.example.banking.bankingTransactions.model.Transaction;
import com.example.banking.bankingTransactions.service.TransactionConfirmationService;
import com.example.banking.bankingTransactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MainController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionConfirmationService transactionConfirmationService;

    @PostMapping("/processTransactions")
    public Mono<Void> processTransactions(@RequestBody Flux<Transaction> transactions) {
        Mono<Transaction> preparedTransactions = transactionService.prepareTransactions(transactions);

        return preparedTransactions
                .zipWhen(preparedTransaction -> transactionConfirmationService.confirmTransactions(Flux.just(preparedTransaction)))
                .then();
    }
}
