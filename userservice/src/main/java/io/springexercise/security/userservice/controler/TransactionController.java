package io.springexercise.security.userservice.controler;

import io.springexercise.security.userservice.Domain.Transaction;
import io.springexercise.security.userservice.Domain.User;
import io.springexercise.security.userservice.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
   private final TransactionService transactionService;
    @PostMapping("/save")
    public ResponseEntity<Transaction> saveUser(@RequestBody Transaction user){

        return ResponseEntity.ok().body(transactionService.saveUser(user));
    }
}
