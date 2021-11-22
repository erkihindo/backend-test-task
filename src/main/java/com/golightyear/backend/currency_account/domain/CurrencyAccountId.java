package com.golightyear.backend.currency_account.domain;

import com.golightyear.backend.common.SerializableValue;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CurrencyAccountId implements SerializableValue<UUID> {

    UUID value;

    public CurrencyAccountId(UUID value) {
        this.value = value;
    }

    public CurrencyAccountId(String value) {
        this.value = UUID.fromString(value);
    }

    public static CurrencyAccountId random() {
        return new CurrencyAccountId(UUID.randomUUID());
    }

}
