package com.pismo.creditservice.services;

import com.pismo.creditservice.errors.AccountNotFoundException;
import com.pismo.creditservice.errors.TransactionTypeNotFound;
import com.pismo.creditservice.helpers.AccountMockBuilder;
import com.pismo.creditservice.helpers.TransactionMockBuilder;
import com.pismo.creditservice.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionServiceImplTest {
    TransactionRepository repository;
    ITransactionService service;
    IAccountService accountService;

    @BeforeEach
    void setUp() {
        repository = mock(TransactionRepository.class);
        accountService = mock(IAccountService.class);
        service = new TransactionServiceImpl(repository, accountService);
    }

    @Nested
    class createTests {
        @Test
        void shouldCreateATransaction() {
            final var transaction = TransactionMockBuilder.mockDefaultValues();
            final var account = AccountMockBuilder.mockDefaultValues();
            when(repository.save(any())).thenReturn(transaction);
            when(accountService.findById(any())).thenReturn(Optional.of(account));
            final var result = service.create(transaction);
            assertEquals(transaction, result);
        }

        @Test
        void shouldTrowsAnExceptionWhenNotFoundAccount() {
            final var transaction = TransactionMockBuilder.mockDefaultValues();
            when(repository.save(any())).thenReturn(transaction);
            when(accountService.findById(any())).thenReturn(Optional.empty());
            assertThrows(AccountNotFoundException.class, () -> service.create(transaction));
        }

        @Test
        void shouldTrowsAnExceptionWhenTypeIsNull() {
            final var transaction = TransactionMockBuilder.mockDefaultValues();
            transaction.setType(null);
            final var account = AccountMockBuilder.mockDefaultValues();
            when(repository.save(any())).thenReturn(transaction);
            when(accountService.findById(any())).thenReturn(Optional.of(account));
            assertThrows(TransactionTypeNotFound.class, () -> service.create(transaction));
        }
    }
}