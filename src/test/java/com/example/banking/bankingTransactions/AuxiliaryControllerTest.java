package com.example.banking.bankingTransactions;

import com.example.banking.bankingTransactions.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuxiliaryControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void calculateFees(){
        Transaction request = new Transaction();
        request.setFee(0);
        request.setAmount(100);
        webTestClient.post()
                .uri("/calculateFees")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.fee").isEqualTo(2.0);

    }

    @Test
    void executeBatch(){
        Transaction t1 = new Transaction();
        Transaction t2 = new Transaction();

        webTestClient.post()
                .uri("/executeBatch")
                .bodyValue(List.of(t1, t2))
                .exchange()
                .expectStatus().isOk();

    }
}