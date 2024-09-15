package com.example.banking.bankingTransactions.service;

import com.example.banking.bankingTransactions.model.Transaction;
import com.example.banking.bankingTransactions.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    /**
     * 1. Prueba para TransactionService:
     * Esta prueba validará el método prepareTransactions, que valida, calcula comisiones y agrega transacciones.
     *
     * Objetivos de la prueba:
     *
     * Simular una lista de transacciones (Flux<Transaction>) con diferentes montos.
     * Validar que las transacciones con monto negativo lancen una excepción (IllegalArgumentException).
     * Mockear la llamada a la API externa en el método calculateFees.
     * Validar que las transacciones se agreguen correctamente y se guarden en el repositorio.
     * Pasos para la prueba:
     *
     * Usar @Mock para mockear el TransactionRepository y el WebClient.Builder.
     * Mockear la respuesta de la API /calculateFees con valores de comisiones simulados.
     * Verificar que se llame al método save del repositorio con una transacción que tiene el monto y la comisión correctos.
     */

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec uriSpec;

    @Mock
    private WebClient.RequestBodyUriSpec headerSpec;

    @InjectMocks
    private TransactionService transactionService;

    //Validar que las transacciones con monto negativo lancen una excepción (IllegalArgumentException).
    @Test
    void shouldThrowExceptionWhenTransactionAmountIsNegative() {
        Transaction transaction = new Transaction();
        transaction.setId("1");
        transaction.setAmount(-100.0);
        transaction.setFee(0.0);

        Flux<Transaction> transactions = Flux.just(transaction);
        assertThrows(IllegalArgumentException.class, () -> transactionService.prepareTransactions(transactions).block());
    }

    //Validar que las transacciones se agreguen correctamente y se guarden en el repositorio.
    @Test
    void shouldAggregateTransactionsAndSave() {
        Transaction transaction1 = new Transaction();
        transaction1.setId("1");
        transaction1.setAmount(100.0);
        transaction1.setFee(1.0);

        Transaction transaction2 = new Transaction();
        transaction2.setId("2");
        transaction2.setAmount(200.0);
        transaction2.setFee(1.0);

        Flux<Transaction> transactions = Flux.just(transaction1, transaction2);

        WebClient.RequestBodyUriSpec bodySpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.ResponseSpec response = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClientBuilder.build().post()).thenReturn(uriSpec);
        when(webClientBuilder.build().post().uri(anyString())).thenReturn(headerSpec);
        doReturn(bodySpec).when(headerSpec).bodyValue(ArgumentMatchers.any(Transaction.class));
        when(bodySpec.retrieve()).thenReturn(response);
        when(response.bodyToMono(Transaction.class)).thenReturn(Mono.justOrEmpty(transaction1));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(Mono.justOrEmpty(transaction1));

        transactionService.prepareTransactions(transactions).block();

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        assertEquals(300.0, transaction1.getAmount());
        assertEquals(2.0, transaction1.getFee());
    }

}