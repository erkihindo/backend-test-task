package com.golightyear.backend.transaction;

import static org.springframework.http.ResponseEntity.ok;

import com.golightyear.backend.transaction.domain.CreditTransactionService;
import com.golightyear.backend.transaction.domain.DebitTransactionService;
import com.golightyear.backend.transaction.domain.FundsTransactionCreateRequest;
import com.golightyear.backend.transaction.domain.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("transaction")
public class TransactionController {

    private final DebitTransactionService debitTransactionService;
    private final CreditTransactionService creditTransactionService;

    @PostMapping("/debit")
    public ResponseEntity<TransactionResponse> debit(@RequestBody FundsTransactionCreateRequest request) {

        TransactionResponse response = debitTransactionService.execute(request);
        return ok(response);
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionResponse> credit(@RequestBody FundsTransactionCreateRequest request) {

        TransactionResponse response = creditTransactionService.execute(request);
        return ok(response);
    }
}
