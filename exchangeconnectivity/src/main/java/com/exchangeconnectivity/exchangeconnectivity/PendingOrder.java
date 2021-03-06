package com.exchangeconnectivity.exchangeconnectivity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingOrder {
    public String product;
    public int quantity;
    public double price;
    public String side;
    public int cumulativeQuantity;
    public String exchange;

    public PendingOrder() {
    }

    public PendingOrder(String product, int quantity, double price, String side, int cumulativeQuantity, String exchange) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.cumulativeQuantity = cumulativeQuantity;
        this.exchange = exchange;
    }
}
