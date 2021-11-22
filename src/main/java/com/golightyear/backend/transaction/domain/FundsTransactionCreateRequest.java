package com.golightyear.backend.transaction.domain;

import java.math.BigDecimal;
import java.util.List;
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
public class FundsTransactionCreateRequest {

    String accountId; // This should be gotten out of SecurityContext, none exists yet
    String currencyAccountId;
    BigDecimal amount;

    // Card or bank account details from or where funds will move.
    // Not really important here
    List<String> bankDetails;
}
