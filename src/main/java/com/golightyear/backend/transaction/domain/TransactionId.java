package com.golightyear.backend.transaction.domain;

import com.golightyear.backend.common.SerializableValue;
import java.util.UUID;
import lombok.Value;

@Value
public class TransactionId implements SerializableValue<UUID> {

    UUID value;

    public TransactionId(UUID value) {
        this.value = value;
    }

    public TransactionId(String value) {
        this.value = UUID.fromString(value);
    }

    public static TransactionId random() {
        return new TransactionId(UUID.randomUUID());
    }

}
