package br.com.krejci.payments.infrastructure.adapter.out.persistence;

import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;
import br.com.krejci.payments.infrastructure.adapter.out.persistence.entity.PaymentEntity;
import br.com.krejci.payments.infrastructure.adapter.out.persistence.mapper.PaymentEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MongoPaymentRepositoryAdapterTest {

    private MongoPaymentRepository mongoRepository;
    private MongoPaymentRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        mongoRepository = mock(MongoPaymentRepository.class);
        adapter = new MongoPaymentRepositoryAdapter(mongoRepository, new PaymentEntityMapper());
    }

    @Test
    void saveMapsToEntityAndMapsSavedEntityBack() {
        Payment payment = payment("tx-save");
        when(mongoRepository.save(any(PaymentEntity.class))).thenAnswer(invocation -> {
            PaymentEntity entity = invocation.getArgument(0);
            entity.setId("mongo-id");
            return entity;
        });

        Payment saved = adapter.save(payment);

        assertEquals("mongo-id", saved.getId().getValue());
        assertEquals(payment.getTransactionId(), saved.getTransactionId());
        verify(mongoRepository).save(any(PaymentEntity.class));
    }

    @Test
    void findByTransactionIdMapsFoundEntity() {
        PaymentEntity entity = new PaymentEntityMapper().toEntity(payment("tx-found"));
        entity.setId("mongo-id");
        when(mongoRepository.findByTransactionId("tx-found")).thenReturn(Optional.of(entity));

        Optional<Payment> result = adapter.findByTransactionId(new TransactionId("tx-found"));

        assertEquals("mongo-id", result.orElseThrow().getId().getValue());
        assertEquals("tx-found", result.orElseThrow().getTransactionId().getValue());
    }

    @Test
    void findByTransactionIdReturnsEmptyWhenNotFound() {
        when(mongoRepository.findByTransactionId("missing")).thenReturn(Optional.empty());

        Optional<Payment> result = adapter.findByTransactionId(new TransactionId("missing"));

        assertFalse(result.isPresent());
    }

    @Test
    void findAllUsesPageableAndMapsPageMetadata() {
        PaymentEntity first = new PaymentEntityMapper().toEntity(payment("tx-1"));
        PaymentEntity second = new PaymentEntityMapper().toEntity(payment("tx-2"));
        PageRequest pageable = PageRequest.of(1, 2, Sort.by(Sort.Direction.ASC, "id"));
        when(mongoRepository.findAll(pageable))
                .thenReturn(new PageImpl<>(List.of(first, second), pageable, 5));

        var result = adapter.findAll(1, 2);

        assertEquals(List.of("tx-1", "tx-2"),
                result.content().stream().map(payment -> payment.getTransactionId().getValue()).toList());
        assertEquals(1, result.page());
        assertEquals(2, result.size());
        assertEquals(5, result.totalElements());
        verify(mongoRepository).findAll(pageable);
    }

    private Payment payment(String transactionId) {
        return new Payment(
                null, new TransactionId(transactionId), "SKU-1", 1234, "12/30",
                123, "Maria", "Silva", "maria@example.com", "+5511999999999", 500
        );
    }
}
