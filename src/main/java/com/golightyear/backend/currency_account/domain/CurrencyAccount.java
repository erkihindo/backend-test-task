package com.golightyear.backend.currency_account.domain;

import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.core.Currency;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
public class CurrencyAccount {

    @Builder.Default
    CurrencyAccountId id = CurrencyAccountId.random();

    AccountId accountId;

    Currency currency;

    BigDecimal amount;

    Boolean isActive;

    ZonedDateTime createdAt;

    ZonedDateTime updatedAt;
}
