package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.DetailedTransactionResponseDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnonymizationService {

    public List<DetailedTransactionResponseDto> applyKAnonymity(List<DetailedTransactionResponseDto> data, int k) {
        // Simple suppression of userId for demonstration
        Map<Integer, List<DetailedTransactionResponseDto>> groups = data.stream()
                .collect(Collectors.groupingBy(DetailedTransactionResponseDto::getUserId));
        List<DetailedTransactionResponseDto> result = new ArrayList<>();
        for (List<DetailedTransactionResponseDto> group : groups.values()) {
            if (group.size() < k) {
                for (DetailedTransactionResponseDto dto : group) {
                    dto.setUserId(null); // Suppress userId
                    result.add(dto);
                }
            } else {
                result.addAll(group);
            }
        }
        return result;
    }

    public List<DetailedTransactionResponseDto> applyLDiversity(List<DetailedTransactionResponseDto> data, int l) {
        // Simple: keep only groups where 'merchant' has at least l distinct values
        Map<Integer, List<DetailedTransactionResponseDto>> groups = data.stream()
                .collect(Collectors.groupingBy(DetailedTransactionResponseDto::getUserId));
        List<DetailedTransactionResponseDto> result = new ArrayList<>();
        for (List<DetailedTransactionResponseDto> group : groups.values()) {
            Set<String> merchants = group.stream().map(DetailedTransactionResponseDto::getMerchant).collect(Collectors.toSet());
            if (merchants.size() >= l) {
                result.addAll(group);
            }
        }
        return result;
    }

    public List<DetailedTransactionResponseDto> applyDifferentialPrivacy(List<DetailedTransactionResponseDto> data, double epsilon) {
        // Add Laplace noise to 'amount'
        Random rand = new Random();
        double scale = 1.0 / epsilon;
        return data.stream().map(dto -> {
            DetailedTransactionResponseDto copy = new DetailedTransactionResponseDto(
                    dto.getId(), dto.getUserId(), dto.getTransactionCategoryId(), dto.getName(), dto.getType(),
                    dto.getAmount() + laplaceNoise(rand, scale),
                    dto.getSourceAccountId(), dto.getSourceAccountBankName(), dto.getDestinationAccountId(),
                    dto.getDestinationAccountBankName(), dto.getMerchant(), dto.getDetails(), dto.getDate(),
                    dto.getUrl(), dto.getSourceAccountCurrency(), dto.getDestinationAccountCurrency()
            );
            return copy;
        }).collect(Collectors.toList());
    }

    public List<DetailedTransactionResponseDto> applyNoiseAddition(List<DetailedTransactionResponseDto> data, double noiseLevel) {
        // Add uniform noise to 'amount'
        Random rand = new Random();
        return data.stream().map(dto -> {
            DetailedTransactionResponseDto copy = new DetailedTransactionResponseDto(
                    dto.getId(), dto.getUserId(), dto.getTransactionCategoryId(), dto.getName(), dto.getType(),
                    dto.getAmount() + (rand.nextDouble() * 2 - 1) * noiseLevel,
                    dto.getSourceAccountId(), dto.getSourceAccountBankName(), dto.getDestinationAccountId(),
                    dto.getDestinationAccountBankName(), dto.getMerchant(), dto.getDetails(), dto.getDate(),
                    dto.getUrl(), dto.getSourceAccountCurrency(), dto.getDestinationAccountCurrency()
            );
            return copy;
        }).collect(Collectors.toList());
    }

    public List<DetailedTransactionResponseDto> applyPseudonymization(List<DetailedTransactionResponseDto> data) {
        // Replace userId with a hash
        return data.stream().map(dto -> {
            DetailedTransactionResponseDto copy = new DetailedTransactionResponseDto(
                    dto.getId(), Objects.hash(dto.getUserId()), dto.getTransactionCategoryId(), dto.getName(), dto.getType(),
                    dto.getAmount(), dto.getSourceAccountId(), dto.getSourceAccountBankName(), dto.getDestinationAccountId(),
                    dto.getDestinationAccountBankName(), dto.getMerchant(), dto.getDetails(), dto.getDate(),
                    dto.getUrl(), dto.getSourceAccountCurrency(), dto.getDestinationAccountCurrency()
            );
            return copy;
        }).collect(Collectors.toList());
    }

    // Helper for Laplace noise
    private double laplaceNoise(Random rand, double scale) {
        double u = rand.nextDouble() - 0.5;
        return -scale * Math.signum(u) * Math.log(1 - 2 * Math.abs(u));
    }
}