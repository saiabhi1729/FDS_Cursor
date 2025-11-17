package com.company.transaction.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.transaction.api.dto.PagedResponse;
import com.company.transaction.api.dto.TransactionRequest;
import com.company.transaction.api.dto.TransactionResponse;
import com.company.transaction.application.TransactionService;
import com.company.transaction.domain.model.TransactionStatus;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/transactions")
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/{id}")
    public Mono<TransactionResponse> getById(@PathVariable UUID id) {
        return transactionService.getTransaction(id);
    }

    @GetMapping
    public Mono<PagedResponse<TransactionResponse>> listByMerchant(
            @RequestParam String merchantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        return transactionService.getTransactionsForMerchant(merchantId, page, size);
    }

    @PatchMapping("/{id}/status")
    public Mono<TransactionResponse> updateStatus(@PathVariable UUID id, @RequestParam TransactionStatus status) {
        return transactionService.updateStatus(id, status);
    }
}
