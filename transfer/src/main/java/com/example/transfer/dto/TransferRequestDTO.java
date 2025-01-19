package com.example.transfer.dto;

import java.util.List;

public class TransferRequestDTO {
    private int maxWeight;
    private List<TransferDTO> availableTransfers;

    public TransferRequestDTO() {}

    public TransferRequestDTO(int maxWeight, List<TransferDTO> availableTransfers) {
        this.maxWeight = maxWeight;
        this.availableTransfers = availableTransfers;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public List<TransferDTO> getAvailableTransfers() {
        return availableTransfers;
    }

    public void setAvailableTransfers(List<TransferDTO> availableTransfers) {
        this.availableTransfers = availableTransfers;
    }
}