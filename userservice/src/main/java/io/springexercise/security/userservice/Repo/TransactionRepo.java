package io.springexercise.security.userservice.Repo;

import io.springexercise.security.userservice.Domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction,Long> {
}
