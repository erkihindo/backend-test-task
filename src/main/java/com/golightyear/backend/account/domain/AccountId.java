package com.golightyear.backend.account.domain;

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
public class AccountId implements SerializableValue<UUID> {

    @ToString.Include
    UUID value;

    public AccountId(UUID value) {
        this.value = value;
    }

    public AccountId(String value) {
        this.value = UUID.fromString(value);
    }

    public static AccountId random() {
        return new AccountId(UUID.randomUUID());
    }

}
