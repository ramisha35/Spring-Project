package com.example.transfer.dto;

import java.util.List;

public class TransferResponseDTO {
    private List<TransferDTO> selectedTransfers;
    private int totalCost;
    private int totalWeight;

    public TransferResponseDTO(List<TransferDTO> selectedTransfers, int totalCost, int totalWeight) {
        this.selectedTransfers = selectedTransfers;
        this.totalCost = totalCost;
        this.totalWeight = totalWeight;
    }

    public List<TransferDTO> getSelectedTransfers() {
        return selectedTransfers;
    }

    public void setSelectedTransfers(List<TransferDTO> selectedTransfers) {
        this.selectedTransfers = selectedTransfers;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }
}