package com.example.banking.bankingTransactions.controlador;

import com.example.banking.bankingTransactions.model.Transaction;
import com.example.banking.bankingTransactions.controller.AuxiliaryController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(AuxiliaryController.class)
class AuxiliaryControllerTest {

    @Autowired
    WebTestClient webTestClient;

    /**
     * 2. Prueba para AuxiliaryController:
     * Esta prueba validará el cálculo de comisiones en el método calculateFees y la ejecución en lote de transacciones.
     *
     * Objetivos de la prueba:
     *
     * Probar el endpoint /calculateFees y verificar que aplique correctamente la comisión del 2%.
     * Probar el endpoint /executeBatch y validar que se imprimen las transacciones, pero que no hay ningún retorno (proceso simulado).
     * Pasos para la prueba:
     *
     * Usar WebTestClient para realizar una llamada POST a /calculateFees con un cuerpo de transacción y verificar que el monto y la comisión en la respuesta sean correctos.
     * Usar WebTestClient para la prueba del batch y verificar que el resultado sea vacío (Mono.empty()).
     */

    //Probar el endpoint /calculateFees y verificar que aplique correctamente la comisión del 2%.
    @Test
    void shouldCalculateFee() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAmount(100.0);
        transaction.setFee(0.0);

        webTestClient.post()
                .uri("/calculateFees")
                .bodyValue(transaction)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.fee").isEqualTo(2.0);
    }

    //Probar el endpoint /executeBatch y validar que se imprimen las transacciones, pero que no hay ningún retorno (proceso simulado).
    @Test
    void shouldExecuteBatch() {
        Transaction transaction1 = new Transaction();
        transaction1.setId("1");
        transaction1.setAmount(100.0);
        transaction1.setFee(2.0);

        Transaction transaction2 = new Transaction();
        transaction2.setId("2");
        transaction2.setAmount(200.0);
        transaction2.setFee(4.0);

        Transaction transaction3 = new Transaction();
        transaction3.setId("3");
        transaction3.setAmount(300.0);
        transaction3.setFee(6.0);

        webTestClient.post()
                .uri("/executeBatch")
                .bodyValue(List.of(transaction1, transaction2, transaction3))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

}