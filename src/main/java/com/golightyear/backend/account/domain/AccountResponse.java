package com.golightyear.backend.account.domain;

import java.time.Instant;
import lombok.Value;

@Value
public class AccountResponse {

    AccountId id;
    AccountName name;
    AccountState state;
    Instant createTime;
    Instant lastModified;

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.id(),
                account.name(),
                account.state(),
                account.createTime(),
                account.lastModified()
        );
    }
}

