package com.example.transfer.service;

import com.example.transfer.dto.TransferDTO;
import com.example.transfer.dto.TransferRequestDTO;
import com.example.transfer.dto.TransferResponseDTO;
import com.example.transfer.entity.Transfer;
import com.example.transfer.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Transactional
    public Transfer saveTransfer(TransferDTO transferDTO) {
        Transfer transfer = new Transfer(transferDTO.getWeight(), transferDTO.getCost());
        return transferRepository.save(transfer);
    }

    @Transactional(readOnly = true)
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Transfer> getTransfersByMaxWeight(int maxWeight) {
        return transferRepository.findByWeightLessThanEqual(maxWeight);
    }

    public TransferResponseDTO findOptimalTransfers(TransferRequestDTO request) {
        int maxWeight = request.getMaxWeight();
        List<TransferDTO> transfers = request.getAvailableTransfers();
        int n = transfers.size();

        int[][] dp = new int[n + 1][maxWeight + 1];
        boolean[][] selected = new boolean[n + 1][maxWeight + 1];

        for (int i = 1; i <= n; i++) {
            TransferDTO transfer = transfers.get(i - 1);
            for (int w = 0; w <= maxWeight; w++) {
                if (transfer.getWeight() <= w) {
                    int newCost = dp[i - 1][w - transfer.getWeight()] + transfer.getCost();
                    if (newCost > dp[i - 1][w]) {
                        dp[i][w] = newCost;
                        selected[i][w] = true;
                    } else {
                        dp[i][w] = dp[i - 1][w];
                    }
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        List<TransferDTO> selectedTransfers = new ArrayList<>();
        int w = maxWeight;
        int totalWeight = 0;

        for (int i = n; i > 0; i--) {
            if (selected[i][w]) {
                TransferDTO transfer = transfers.get(i - 1);
                selectedTransfers.add(transfer);
                w -= transfer.getWeight();
                totalWeight += transfer.getWeight();
            }
        }

        return new TransferResponseDTO(selectedTransfers, dp[n][maxWeight], totalWeight);
    }

    private TransferDTO convertToDTO(Transfer transfer) {
        return new TransferDTO(transfer.getWeight(), transfer.getCost());
    }

    private Transfer convertToEntity(TransferDTO dto) {
        return new Transfer(dto.getWeight(), dto.getCost());
    }
}