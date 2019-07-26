package me.sadensmol.flights.model;

public class BaggageToDest {
    private Baggage baggage;
    private Destination dest;

    public BaggageToDest(Baggage baggage, Destination dest) {
        this.baggage = baggage;
        this.dest = dest;

    }

    public Baggage getBaggage() {
        return baggage;
    }

    public Destination getDest() {
        return dest;
    }
}
