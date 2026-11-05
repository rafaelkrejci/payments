package br.com.krejci.payments.infrastructure.adapter.out.persistence;

import br.com.krejci.payments.application.port.out.PaymentRepository;
import br.com.krejci.payments.application.port.out.PaymentPage;
import br.com.krejci.payments.domain.model.Payment;
import br.com.krejci.payments.domain.vo.TransactionId;
import br.com.krejci.payments.infrastructure.adapter.out.persistence.entity.PaymentEntity;
import br.com.krejci.payments.infrastructure.adapter.out.persistence.mapper.PaymentEntityMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MongoPaymentRepositoryAdapter implements PaymentRepository {

    private final MongoPaymentRepository mongoRepository;
    private final PaymentEntityMapper mapper;

    public MongoPaymentRepositoryAdapter(MongoPaymentRepository mongoRepository, PaymentEntityMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        return mapper.toDomain(mongoRepository.save(mapper.toEntity(payment)));
    }

    @Override
    public Optional<Payment> findByTransactionId(TransactionId transactionId) {
        return mongoRepository.findByTransactionId(transactionId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public PaymentPage findAll(int page, int size) {
        org.springframework.data.domain.Page<PaymentEntity> result = mongoRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))
        );
        return new PaymentPage(
                result.getContent().stream()
                .map(mapper::toDomain)
                .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements()
        );
    }
}
