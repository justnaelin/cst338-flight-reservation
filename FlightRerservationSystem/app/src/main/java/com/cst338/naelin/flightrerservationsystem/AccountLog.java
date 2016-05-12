package com.cst338.naelin.flightrerservationsystem;


public class AccountLog
{
    private int id;
    private String transactionType;
    private String username;
    private String timestamp;

    public AccountLog()
    {
        id = 0;
        transactionType = "UNKNOWN";
        username = "UNKNOWN";
        timestamp = "UNKNOWN";
    }

    public AccountLog(String transactionType, String username, String timestamp)
    {
        this.transactionType = transactionType;
        this.username = username;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AccountLog{" +
                "id=" + id +
                ", transactionType='" + transactionType + '\'' +
                ", username='" + username + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
