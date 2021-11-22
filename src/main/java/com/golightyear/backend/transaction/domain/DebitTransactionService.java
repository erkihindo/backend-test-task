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
public class DebitTransactionService {

    private final TransactionRepository transactionRepository;
    private final CurrencyAccountRepository currencyAccountRepository;

    public TransactionResponse execute(FundsTransactionCreateRequest request) {
        Transaction debitTransaction = buildTransaction(request);
        transactionRepository.add(debitTransaction);

        Optional<CurrencyAccount> currencyAccountOptional = currencyAccountRepository.find(
            new CurrencyAccountId(request.currencyAccountId()));
        CurrencyAccount currencyAccount = currencyAccountOptional
            .orElseThrow(() -> new RuntimeException("Currency account not found for id " + request.currencyAccountId()));

        BigDecimal newAmount = currencyAccount.amount().subtract(request.amount());
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Can not credit more than on account");
        }

        currencyAccountRepository.updateAmount(currencyAccount.id(), newAmount);
        transactionRepository.updateState(debitTransaction.id(), TransactionState.COMPLETED);

        return TransactionResponse.from(debitTransaction);
    }

    private Transaction buildTransaction(FundsTransactionCreateRequest request) {
        return Transaction.builder()
            .sourceType(EntityType.CURRENCY_ACCOUNT)
            .sourceId(new CurrencyAccountId(request.currencyAccountId()).value())
            .destinationType(EntityType.ACCOUNT)
            .destinationId(new AccountId(request.accountId()).value())
            .state(TransactionState.NEW)
            .build();
    }
}
