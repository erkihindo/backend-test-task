package com.golightyear.backend.transaction;

import com.golightyear.backend.transaction.domain.Transaction;
import com.golightyear.backend.transaction.domain.TransactionId;
import com.golightyear.backend.transaction.domain.TransactionState;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    void add(Transaction transaction);

    List<Transaction> findAll();

    Optional<Transaction> find(TransactionId id);

    int updateState(TransactionId id, TransactionState state);

}
