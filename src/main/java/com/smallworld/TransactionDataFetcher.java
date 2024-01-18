package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionDataFetcher {

    private List<Transaction> transactions;

    public TransactionDataFetcher(List<Transaction> transactions) {
        // Load transactions from JSON file during class instantiation
        try (InputStream inputStream = getClass().getResourceAsStream("/transactions.json")) {
            ObjectMapper objectMapper = new ObjectMapper();
            this.transactions = objectMapper.readValue(inputStream, new TypeReference<List<Transaction>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getTotalTransactionAmount() {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalTransactionAmountSentBy(String senderFullName) {
        return transactions.stream()
                .filter(transaction -> transaction.getSenderFullName().equals(senderFullName))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getMaxTransactionAmount() {
        return transactions.stream().mapToDouble(Transaction::getAmount).max().orElse(0.0);
    }

    public long countUniqueClients() {
        Set<String> uniqueClients = transactions.stream()
                .flatMap(transaction -> Set.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName()).stream())
                .collect(Collectors.toSet());
        return uniqueClients.size();
    }

    public boolean hasOpenComplianceIssues(String clientFullName) {
        return transactions.stream()
                .anyMatch(transaction -> (clientFullName.equals(transaction.getSenderFullName())
                        || clientFullName.equals(transaction.getBeneficiaryFullName()))
                        && transaction.getIssueSolved() != null
                        && !transaction.getIssueSolved());
    }

    public Map<String, List<Transaction>> getTransactionsByBeneficiaryName() {
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName));
    }

    public Set<Integer> getUnsolvedIssueIds() {
        return transactions.stream()
                .filter(transaction -> transaction.getIssueId() != null && !transaction.getIssueSolved())
                .map(Transaction::getIssueId)
                .collect(Collectors.toSet());
    }

    public List<String> getAllSolvedIssueMessages() {
        return transactions.stream()
                .filter(transaction -> transaction.getIssueSolved() != null && transaction.getIssueSolved())
                .map(Transaction::getIssueMessage)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTop3TransactionsByAmount() {
        return transactions.stream()
                .sorted(Comparator.comparingDouble(Transaction::getAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public Optional<Transaction> getTopSender() {
        Map<String, Double> senderTotalAmount = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSenderFullName, Collectors.summingDouble(Transaction::getAmount)));

        return senderTotalAmount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> transactions.stream()
                        .filter(transaction -> transaction.getSenderFullName().equals(entry.getKey()))
                        .findFirst()
                        .orElse(null));
    }
}
