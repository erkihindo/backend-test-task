package com.golightyear.backend.transaction.domain;

import com.golightyear.core.EntityType;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class TransactionResponse {

    TransactionId id;

    EntityType sourceType;

    UUID sourceId;

    EntityType destinationType;

    UUID destinationId;

    TransactionState state;

    public static TransactionResponse from(Transaction transaction) {
        return new TransactionResponse(
            transaction.id(),
            transaction.sourceType(),
            transaction.sourceId(),
            transaction.destinationType(),
            transaction.destinationId(),
            transaction.state()
        );
    }
}
