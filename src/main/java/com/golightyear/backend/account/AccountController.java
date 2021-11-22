package com.golightyear.backend.account;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.ok;

import com.golightyear.backend.account.domain.Account;
import com.golightyear.backend.account.domain.AccountCreateRequest;
import com.golightyear.backend.account.domain.AccountId;
import com.golightyear.backend.account.domain.AccountName;
import com.golightyear.backend.account.domain.AccountResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Account> add(@RequestBody AccountCreateRequest request) {
        final var account = Account.builder()
                .name(new AccountName(request.name()))
                .build();

        accountRepository.add(account);
        return ok(account);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        final var responses = accountRepository.findAll().stream()
                .map(AccountResponse::from)
                .collect(toList());

        return ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> find(@PathVariable("id") String id) {
        return accountRepository
                .find(new AccountId(id))
                .map(AccountResponse::from)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    }

}
