package me.sadensmol.flights.model;

public class Coupon {
    private int id;
    private double price;

    public Coupon(int id, double price) {
        this.id = id;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Coupon " + id + ", price : " + price;
    }
}
