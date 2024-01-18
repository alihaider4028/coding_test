package com.smallworld;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

    @JsonProperty("mtn")
    private long mtn;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("senderFullName")
    private String senderFullName;

    @JsonProperty("senderAge")
    private int senderAge;

    @JsonProperty("beneficiaryFullName")
    private String beneficiaryFullName;

    @JsonProperty("beneficiaryAge")
    private int beneficiaryAge;

    @JsonProperty("issueId")
    private Integer issueId;

    @JsonProperty("issueSolved")
    private Boolean issueSolved;

    @JsonProperty("issueMessage")
    private String issueMessage;

    // Getters for all fields

    public long getMtn() {
        return mtn;
    }

    public double getAmount() {
        return amount;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public int getSenderAge() {
        return senderAge;
    }

    public String getBeneficiaryFullName() {
        return beneficiaryFullName;
    }

    public int getBeneficiaryAge() {
        return beneficiaryAge;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public Boolean getIssueSolved() {
        return issueSolved;
    }

    public String getIssueMessage() {
        return issueMessage;
    }
}
