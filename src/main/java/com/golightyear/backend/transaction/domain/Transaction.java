package com.golightyear.backend.transaction.domain;

import com.golightyear.core.EntityType;
import java.time.ZonedDateTime;
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
public class Transaction {

    @Builder.Default
    TransactionId id = TransactionId.random();

    EntityType sourceType;

    UUID sourceId;

    EntityType destinationType;

    UUID destinationId;

    TransactionState state;

    ZonedDateTime createdAt;

    ZonedDateTime updatedAt;
}
