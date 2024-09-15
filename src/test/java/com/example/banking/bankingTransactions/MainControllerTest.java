package com.example.banking.bankingTransactions;

import com.example.banking.bankingTransactions.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MainControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void processTransaction() {
        Transaction request = new Transaction();
        request.setFee(2.0);
        request.setAmount(100);

        webTestClient.post()
                .uri("/processTransactions")
                .bodyValue(List.of(request))
                .exchange()
                .expectStatus().isOk();
    }
}