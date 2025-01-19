package com.example.transfer.controller;

import com.example.transfer.dto.TransferDTO;
import com.example.transfer.dto.TransferRequestDTO;
import com.example.transfer.dto.TransferResponseDTO;
import com.example.transfer.entity.Transfer;
import com.example.transfer.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/optimize")
    public ResponseEntity<TransferResponseDTO> optimizeTransfers(@RequestBody TransferRequestDTO request) {
        TransferResponseDTO result = transferService.findOptimalTransfers(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Transfer> createTransfer(@RequestBody TransferDTO transferDTO) {
        Transfer savedTransfer = transferService.saveTransfer(transferDTO);
        return ResponseEntity.ok(savedTransfer);
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        List<Transfer> transfers = transferService.getAllTransfers();
        return ResponseEntity.ok(transfers);
    }

    @GetMapping("/weight/{maxWeight}")
    public ResponseEntity<List<Transfer>> getTransfersByMaxWeight(@PathVariable int maxWeight) {
        List<Transfer> transfers = transferService.getTransfersByMaxWeight(maxWeight);
        return ResponseEntity.ok(transfers);
    }
}