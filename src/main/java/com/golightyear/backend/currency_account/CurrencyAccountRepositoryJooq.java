package com.golightyear.backend.currency_account;

import static com.lightyear.generated.Tables.CURRENCY_ACCOUNT;

import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.currency_account.domain.CurrencyAccount;
import com.golightyear.backend.currency_account.domain.CurrencyAccountId;
import com.golightyear.core.Currency;
import com.lightyear.generated.tables.records.CurrencyAccountRecord;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

@Component
public class CurrencyAccountRepositoryJooq implements CurrencyAccountRepository {

    private final DSLContext context;

    public CurrencyAccountRepositoryJooq(DSLContext context) {
        this.context = context;
    }

    @Override
    public void add(CurrencyAccount currencyAccount) {
        context.executeInsert(toRecord(currencyAccount));
    }

    @Override
    public List<CurrencyAccount> findAll() {
        return context.selectFrom(CURRENCY_ACCOUNT)
            .fetch(CurrencyAccountRepositoryJooq::mapRecord);
    }

    @Override
    public int updateAmount(CurrencyAccountId id, BigDecimal amount) {
        return context.update(CURRENCY_ACCOUNT)
            .set(CURRENCY_ACCOUNT.AMOUNT, amount.toString())
            .set(CURRENCY_ACCOUNT.LAST_MODIFIED, LocalDateTime.now())
            .where(CURRENCY_ACCOUNT.ID.eq(id.value()))
            .execute();
    }

    @Override
    public Optional<CurrencyAccount> find(CurrencyAccountId id) {
        return context.selectFrom(CURRENCY_ACCOUNT)
            .where(CURRENCY_ACCOUNT.ID.eq(id.value()))
            .fetchOptional(CurrencyAccountRepositoryJooq::mapRecord);
    }

    private static CurrencyAccountRecord toRecord(CurrencyAccount currencyAccount) {
        return new CurrencyAccountRecord(
            currencyAccount.id().value(),
            currencyAccount.accountId().value(),
            currencyAccount.currency().name(),
            currencyAccount.amount().toString(),
            Boolean.TRUE,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    private static CurrencyAccount mapRecord(CurrencyAccountRecord record) {
        return CurrencyAccount.builder()
            .id(new CurrencyAccountId(record.getId()))
            .accountId(new AccountId(record.getAccountId()))
            .currency(Currency.valueOf(record.getCurrency()))
            .amount(new BigDecimal(record.getAmount()))
            .isActive(record.getIsActive())
            .createdAt(record.getCreateTime().atZone(ZoneId.systemDefault()))
            .updatedAt(record.getLastModified().atZone(ZoneId.systemDefault()))
            .build();
    }
}
