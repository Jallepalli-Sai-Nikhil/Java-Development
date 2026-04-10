package org.example.entity;

public class Order {
    private String orderId;
    private String product;

    public Order(String orderId, String product) {
        this.orderId = orderId;
        this.product = product;
    }

    public String getOrderId() { return orderId; }
    public String getProduct() { return product; }
}