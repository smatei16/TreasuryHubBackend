package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.DetailedTransactionResponseDto;
import com.treasury.treasuryhub.service.AnonymizationService;
import com.treasury.treasuryhub.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anonymization")
public class AnonymizationController {
    @Autowired
    private AnonymizationService anonymizationService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/k-anonymity/{k}")
    public List<DetailedTransactionResponseDto> applyKAnonymity(
            @PathVariable int k) {
        List<DetailedTransactionResponseDto> data = transactionService.getAllDetailedTransactions();
        return anonymizationService.applyKAnonymity(data, k);
    }

    @GetMapping("/l-diversity/{l}")
    public List<DetailedTransactionResponseDto> applyLDiversity(
            @PathVariable int l) {
        List<DetailedTransactionResponseDto> data = transactionService.getAllDetailedTransactions();
        return anonymizationService.applyLDiversity(data, l);
    }

    @GetMapping("/differential-privacy/{epsilon}")
    public List<DetailedTransactionResponseDto> applyDifferentialPrivacy(
            @PathVariable double epsilon) {
        List<DetailedTransactionResponseDto> data = transactionService.getAllDetailedTransactions();
        return anonymizationService.applyDifferentialPrivacy(data, epsilon);
    }

    @GetMapping("/noise-addition/{noiseLevel}")
    public List<DetailedTransactionResponseDto> applyNoiseAddition(
            @PathVariable double noiseLevel) {
        List<DetailedTransactionResponseDto> data = transactionService.getAllDetailedTransactions();
        return anonymizationService.applyNoiseAddition(data, noiseLevel);
    }

    @GetMapping("/pseudonymization")
    public List<DetailedTransactionResponseDto> applyPseudonymization() {
        List<DetailedTransactionResponseDto> data = transactionService.getAllDetailedTransactions();
        return anonymizationService.applyPseudonymization(data);
    }
}
