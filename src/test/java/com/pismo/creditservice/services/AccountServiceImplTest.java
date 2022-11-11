package com.pismo.creditservice.services;

import com.pismo.creditservice.errors.InvalidDocumentNumberException;
import com.pismo.creditservice.helpers.AccountMockBuilder;
import com.pismo.creditservice.helpers.MockConstants;
import com.pismo.creditservice.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {
    IAccountService service;
    AccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(AccountRepository.class);
        service = new AccountServiceImpl(repository);
    }

    @Nested
    class createMethodTests {
        @Test
        void shouldCallRepository() {
            final var account = AccountMockBuilder.mockDefaultValues();
            when(repository.save(any())).thenReturn(account);
            final var result = service.create(MockConstants.MOCKED_DOCUMENT_NUMBER);
            assertEquals(account, result);
            verify(repository).save(any());
        }

        @Test
        void shouldReturnsAnErrorIfDocumentNumberIsInvalid() {
            assertThrows(InvalidDocumentNumberException.class, () -> service.create(null));
        }
    }
}