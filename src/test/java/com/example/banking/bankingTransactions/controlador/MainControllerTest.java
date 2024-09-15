package com.example.banking.bankingTransactions.controlador;

import com.example.banking.bankingTransactions.controller.MainController;
import com.example.banking.bankingTransactions.model.Transaction;
import com.example.banking.bankingTransactions.service.TransactionConfirmationService;
import com.example.banking.bankingTransactions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(MainController.class)
class MainControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionConfirmationService transactionConfirmationService;

    private Transaction request;

    @BeforeEach
    void setup(){
        request = new Transaction();
        request.setId("xxxxx-xxxxxx-xxxx");
        request.setAmount(100);

        Transaction transaction = new Transaction();
        transaction.setId("xxxxx-xxxxxx-xxxx");
        transaction.setAmount(100);

        Mockito.when(transactionService.prepareTransactions(any(Flux.class)))
                .thenReturn(Mono.just(transaction));

        Mockito.when(transactionConfirmationService.confirmTransactions(any(Flux.class)))
                .thenReturn(Mono.empty());

    }

    @Test
    void processTransaction(){

        webTestClient.post()
                .uri("/processTransactions")
                .bodyValue(List.of(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty();

        ArgumentCaptor<Flux<Transaction>> captor = ArgumentCaptor.forClass(Flux.class);

        Mockito.verify(transactionService)
                .prepareTransactions(captor.capture());

        Mockito.verify(transactionConfirmationService, Mockito.times(1))
                .confirmTransactions(any(Flux.class));

        Flux<Transaction> captuered = captor.getValue();
        StepVerifier.create(captuered)
                .expectNextMatches(transaction -> request.toString().equals(transaction.toString()))
                .verifyComplete();


    }
}