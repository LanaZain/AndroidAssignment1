package com.example.assignment1;

public class Product {
    private int id;
    private String name;
    private String petType;
    private String category;
    private String desc;
    private double price;
    private int quantity;
    private boolean homeDelivery;

    public Product(int id, String name, String petType, String category, double price, int quantity, boolean homeDelivery, String desc){
        this.category = category;
        this.name = name;
        this.id = id;
        this.petType = petType;
        this.price = price;
        this.quantity = quantity;
        this.homeDelivery= homeDelivery;
        this.desc= desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public boolean isHomeDelivery() {
        return homeDelivery;
    }

    public void setHomeDelivery(boolean homeDelivery) {
        this.homeDelivery = homeDelivery;
    }

    @Override
    public String toString() {
        return name + " (" + category + ") - $" + String.format("%.2f", price);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
