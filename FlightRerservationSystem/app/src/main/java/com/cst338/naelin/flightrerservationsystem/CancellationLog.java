package com.cst338.naelin.flightrerservationsystem;

/**
 * Created by naelin on 5/12/16.
 */
public class CancellationLog
{
    private String cancellationInfo;
    private int id;
    private String timestamp;

    public CancellationLog() {}

    public CancellationLog(String cancellationInfo) {
        this.cancellationInfo = cancellationInfo;
    }

    public String getCancellationInfo() {
        return cancellationInfo;
    }

    public void setCancellationInfo(String cancellationInfo) {
        this.cancellationInfo = cancellationInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return cancellationInfo;
    }
}
