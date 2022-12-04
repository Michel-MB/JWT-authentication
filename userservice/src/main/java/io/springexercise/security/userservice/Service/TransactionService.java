package io.springexercise.security.userservice.Service;

import io.springexercise.security.userservice.Domain.Transaction;
import io.springexercise.security.userservice.Domain.User;

public interface TransactionService {
    Transaction saveUser(Transaction user);
}
