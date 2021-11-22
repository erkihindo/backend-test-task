package com.golightyear.backend.transaction;

import static com.lightyear.generated.Tables.TRANSACTION;

import com.golightyear.backend.transaction.domain.Transaction;
import com.golightyear.backend.transaction.domain.TransactionId;
import com.golightyear.backend.transaction.domain.TransactionState;
import com.golightyear.core.EntityType;
import com.lightyear.generated.tables.records.TransactionRecord;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

@Component
public class TransactionRepositoryJooq implements TransactionRepository {

    private final DSLContext context;

    public TransactionRepositoryJooq(DSLContext context) {
        this.context = context;
    }

    @Override
    public void add(Transaction transaction) {
        context.executeInsert(toRecord(transaction));
    }

    @Override
    public List<Transaction> findAll() {
        return context.selectFrom(TRANSACTION)
            .fetch(TransactionRepositoryJooq::mapRecord);
    }

    @Override
    public Optional<Transaction> find(TransactionId id) {
        return context.selectFrom(TRANSACTION)
            .where(TRANSACTION.ID.eq(id.value()))
            .fetchOptional(TransactionRepositoryJooq::mapRecord);
    }

    @Override
    public int updateState(TransactionId id, TransactionState state) {
        return context.update(TRANSACTION)
            .set(TRANSACTION.STATE, state.name())
            .set(TRANSACTION.LAST_MODIFIED, LocalDateTime.now())
            .where(TRANSACTION.ID.eq(id.value()))
            .execute();
    }

    private static TransactionRecord toRecord(Transaction transaction) {
        return new TransactionRecord(
            transaction.id().value(),
            transaction.sourceType().name(),
            transaction.sourceId(),
            transaction.destinationType().name(),
            transaction.destinationId(),
            transaction.state().name(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    private static Transaction mapRecord(TransactionRecord record) {
        return Transaction.builder()
            .id(new TransactionId(record.getId()))
            .sourceType(EntityType.valueOf(record.getSourceType()))
            .sourceId(record.getSourceId())
            .destinationType(EntityType.valueOf(record.getDestinationType()))
            .destinationId(record.getDestinationId())
            .state(TransactionState.valueOf(record.getState()))
            .createdAt(record.getCreateTime().atZone(ZoneId.systemDefault()))
            .updatedAt(record.getLastModified().atZone(ZoneId.systemDefault()))
            .build();
    }
}
