package io.springexercise.security.userservice.Service;

import io.springexercise.security.userservice.Domain.Transaction;
import io.springexercise.security.userservice.Repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService {

 private final TransactionRepo transactionRepo;

    @Override
    public Transaction saveUser(Transaction user) {
        return transactionRepo.save(user);
    }
}
