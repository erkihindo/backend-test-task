package com.golightyear.backend.currency_account;

import com.golightyear.backend.currency_account.domain.CurrencyAccount;
import com.golightyear.backend.currency_account.domain.CurrencyAccountId;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CurrencyAccountRepository {

    void add(CurrencyAccount currencyAccount);

    List<CurrencyAccount> findAll();

    Optional<CurrencyAccount> find(CurrencyAccountId id);

    int updateAmount(CurrencyAccountId id, BigDecimal newAmount);

}
