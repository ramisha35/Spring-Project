package com.example.transfer.dto;

public class TransferDTO {
    private int weight;
    private int cost;

    public TransferDTO() {}

    public TransferDTO(int weight, int cost) {
        this.weight = weight;
        this.cost = cost;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}