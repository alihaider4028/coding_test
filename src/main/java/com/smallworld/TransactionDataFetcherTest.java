package com.smallworld;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.Transaction;
import com.smallworld.TransactionDataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionDataFetcherTest {

    private TransactionDataFetcher transactionDataFetcher;

    @BeforeEach
    void setUp() {
        transactionDataFetcher = new TransactionDataFetcher(loadTestTransactions());
    }

    @Test
    void testGetTotalTransactionAmount() {
        double totalAmount = transactionDataFetcher.getTotalTransactionAmount();
        assertEquals(4371.37, totalAmount, 0.01);
    }

    @Test
    void testGetTotalTransactionAmountSentBy() {
        double totalAmount = transactionDataFetcher.getTotalTransactionAmountSentBy("Grace Burgess");
        assertEquals(1998.0, totalAmount, 0.01);
    }

    @Test
    void testGetMaxTransactionAmount() {
        double maxAmount = transactionDataFetcher.getMaxTransactionAmount();
        assertEquals(985.0, maxAmount, 0.01);
    }

    @Test
    void testCountUniqueClients() {
        long uniqueClients = transactionDataFetcher.countUniqueClients();
        assertEquals(14, uniqueClients);
    }

    @Test
    void testHasOpenComplianceIssues() {
        assertTrue(transactionDataFetcher.hasOpenComplianceIssues("Tom Shelby"));
        assertFalse(transactionDataFetcher.hasOpenComplianceIssues("Aunt Polly"));
    }

    @Test
    void testGetTransactionsByBeneficiaryName() {
        Map<String, List<Transaction>> transactionsByBeneficiary = transactionDataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(10, transactionsByBeneficiary.size());
    }

    @Test
    void testGetUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = transactionDataFetcher.getUnsolvedIssueIds();
        assertEquals(Set.of(1, 3, 99, 54, 15), unsolvedIssueIds);
    }

    @Test
    void testGetAllSolvedIssueMessages() {
        List<String> solvedIssueMessages = transactionDataFetcher.getAllSolvedIssueMessages().stream().filter(e->e!=null).collect(Collectors.toList());
        assertEquals(List.of("Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you"), solvedIssueMessages);
    }

    @Test
    void testGetTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = transactionDataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(985.0, top3Transactions.get(0).getAmount(), 0.01);
    }

    @Test
    void testGetTopSender() {
        Optional<Transaction> topSender = transactionDataFetcher.getTopSender();
        assertTrue(topSender.isPresent());
        assertEquals("Grace Burgess", topSender.get().getSenderFullName());
    }
    private List<Transaction> loadTestTransactions() {
        try (InputStream inputStream = getClass().getResourceAsStream("/transactions.json")) {
            if (inputStream == null) {
                throw new IOException("Could not load transactions.json. Make sure the file is in the correct location.");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<List<Transaction>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
