package com.golightyear.backend.transaction.domain;

import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.currency_account.CurrencyAccountRepository;
import com.golightyear.backend.currency_account.domain.CurrencyAccount;
import com.golightyear.backend.currency_account.domain.CurrencyAccountId;
import com.golightyear.backend.transaction.TransactionRepository;
import com.golightyear.core.EntityType;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditTransactionService {

    private final TransactionRepository transactionRepository;
    private final CurrencyAccountRepository currencyAccountRepository;

    public TransactionResponse execute(FundsTransactionCreateRequest request) {
        Transaction creditTransaction = buildTransaction(request);
        transactionRepository.add(creditTransaction);

        Optional<CurrencyAccount> currencyAccountOptional = currencyAccountRepository.find(
            new CurrencyAccountId(request.currencyAccountId()));
        CurrencyAccount currencyAccount = currencyAccountOptional
            .orElseThrow(() -> new RuntimeException("Currency account not found for id " + request.currencyAccountId()));

        BigDecimal newAmount = currencyAccount.amount().add(request.amount());
        currencyAccountRepository.updateAmount(currencyAccount.id(), newAmount);

        transactionRepository.updateState(creditTransaction.id(), TransactionState.COMPLETED);

        return TransactionResponse.from(creditTransaction);
    }

    private Transaction buildTransaction(FundsTransactionCreateRequest request) {
        return Transaction.builder()
            .sourceType(EntityType.ACCOUNT)
            .sourceId(new AccountId(request.accountId()).value())
            .destinationType(EntityType.CURRENCY_ACCOUNT)
            .destinationId(new CurrencyAccountId(request.currencyAccountId()).value())
            .state(TransactionState.NEW)
            .build();
    }
}
