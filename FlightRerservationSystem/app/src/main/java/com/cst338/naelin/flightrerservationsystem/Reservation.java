package com.cst338.naelin.flightrerservationsystem;

/**
 * Created by naelin on 5/11/16.
 */
public class Reservation
{
    private int id;
    private String transactionType;
    private String username;
    private String flightNo;
    private String departure;
    private String arrival;
    private String departureTime;
    private int noOfTickets;
    private String reservationNo;
    private double total;
    private String timestamp;

    public Reservation() {}

    public Reservation(String transactionType, String username, String flightNo, String departure, String arrival, String departureTime, int noOfTickets, String reservationNo, double total, String timestamp) {
        this.transactionType = transactionType;
        this.username = username;
        this.flightNo = flightNo;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.noOfTickets = noOfTickets;
        this.reservationNo = reservationNo;
        this.total = total;
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

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public void setNoOfTickets(int noOfTickets) {
        this.noOfTickets = noOfTickets;
    }

    public String getReservationNo() {
        return reservationNo;
    }

    public void setReservationNo(String reservationNo) {
        this.reservationNo = reservationNo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "transactionType='" + transactionType + '\'' +
                ", username='" + username + '\'' +
                ", flightNo='" + flightNo + '\'' +
                ", departure='" + departure + '\'' +
                ", arrival='" + arrival + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", noOfTickets=" + noOfTickets +
                ", reservationNo='" + reservationNo + '\'' +
                ", total=" + total +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
