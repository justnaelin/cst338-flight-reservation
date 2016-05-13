package com.cst338.naelin.flightrerservationsystem;

public class Flight
{
    private int id;
    private String flightNo;
    private String departure;
    private String arrival;
    private String departureTime;
    private int capacity;
    private double price;

    public Flight() {}

    public Flight(String flightNo, String departure, String arrival, String departureTime, int capacity, double price)
    {
        this.flightNo = flightNo;
        this.departure = departure;
        this.arrival = arrival;
        this.departureTime = departureTime;
        this.capacity = capacity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return
                "Flight No.: " + flightNo + "\n" +
                "Departure: " + departure + "\n" +
                "Arrival: " + arrival + "\n" +
                "Departure Time: " + departureTime + "\n" +
                "Capacity: " + capacity +"\n"+
                "Price: $" + price;
    }
}
