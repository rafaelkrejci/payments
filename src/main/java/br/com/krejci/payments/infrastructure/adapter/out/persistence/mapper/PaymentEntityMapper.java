package br.com.krejci.payments.infrastructure.adapter.out.persistence.mapper;

import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.PaymentId;
import br.com.krejci.payments.domain.vo.TransactionId;
import br.com.krejci.payments.infrastructure.adapter.out.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityMapper {

    public PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId() == null ? null : payment.getId().getValue())
                .transactionId(payment.getTransactionId().getValue())
                .sku(payment.getSku())
                .pan(payment.getPan())
                .expirationDate(payment.getExpirationDate())
                .cvv(payment.getCvv())
                .cardHolderName(payment.getCardHolderName())
                .cardHolderLastName(payment.getCardHolderLastName())
                .cardHolderEmail(payment.getCardHolderEmail())
                .cardHolderPhone(payment.getCardHolderPhone())
                .amount(payment.getAmount())
                .build();
    }

    public Payment toDomain(PaymentEntity entity) {
        return new Payment(
                entity.getId() == null ? null : new PaymentId(entity.getId()),
                new TransactionId(entity.getTransactionId()),
                entity.getSku(),
                entity.getPan(),
                entity.getExpirationDate(),
                entity.getCvv(),
                entity.getCardHolderName(),
                entity.getCardHolderLastName(),
                entity.getCardHolderEmail(),
                entity.getCardHolderPhone(),
                entity.getAmount()
        );
    }
}
