package br.com.krejci.payments.infrastructure.adapter.out.persistence;

import br.com.krejci.payments.infrastructure.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoPaymentRepository extends MongoRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByTransactionId(String transactionId);
}
