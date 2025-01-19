package com.example.transfer;

import com.example.transfer.dto.TransferDTO;
import com.example.transfer.dto.TransferRequestDTO;
import com.example.transfer.dto.TransferResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransferServiceTest {

    @Autowired
    private com.example.transfer.service.TransferService transferService;

    @Nested
    @DisplayName("Basic Functionality Tests")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("Should find optimal solution from example input")
        void testOptimalTransferSelection() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(5, 10),
                    new TransferDTO(10, 20),
                    new TransferDTO(3, 5),
                    new TransferDTO(8, 15)
            );

            TransferRequestDTO request = new TransferRequestDTO(15, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(30, result.getTotalCost());
            assertEquals(15, result.getTotalWeight());
            assertEquals(2, result.getSelectedTransfers().size());
            assertTrue(result.getSelectedTransfers().stream()
                    .anyMatch(t -> t.getWeight() == 5 && t.getCost() == 10));
            assertTrue(result.getSelectedTransfers().stream()
                    .anyMatch(t -> t.getWeight() == 10 && t.getCost() == 20));
        }

        @Test
        @DisplayName("Should handle single transfer case")
        void testSingleTransfer() {
            List<TransferDTO> transfers = Collections.singletonList(
                    new TransferDTO(5, 10)
            );

            TransferRequestDTO request = new TransferRequestDTO(10, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(10, result.getTotalCost());
            assertEquals(5, result.getTotalWeight());
            assertEquals(1, result.getSelectedTransfers().size());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty transfer list")
        void testEmptyTransferList() {
            TransferRequestDTO request = new TransferRequestDTO(15, Collections.emptyList());
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(0, result.getTotalCost());
            assertEquals(0, result.getTotalWeight());
            assertTrue(result.getSelectedTransfers().isEmpty());
        }

        @Test
        @DisplayName("Should handle zero max weight")
        void testZeroMaxWeight() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(5, 10),
                    new TransferDTO(3, 5)
            );

            TransferRequestDTO request = new TransferRequestDTO(0, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(0, result.getTotalCost());
            assertEquals(0, result.getTotalWeight());
            assertTrue(result.getSelectedTransfers().isEmpty());
        }

        @Test
        @DisplayName("Should handle when no transfers fit within max weight")
        void testNoValidTransfers() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(10, 20),
                    new TransferDTO(15, 30)
            );

            TransferRequestDTO request = new TransferRequestDTO(5, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(0, result.getTotalCost());
            assertEquals(0, result.getTotalWeight());
            assertTrue(result.getSelectedTransfers().isEmpty());
        }
    }

    @Nested
    @DisplayName("Optimization Strategy Tests")
    class OptimizationStrategyTests {

        @Test
        @DisplayName("Should prefer higher value items when weight is constrained")
        void testHigherValuePreference() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(5, 20),  // Better value/weight ratio
                    new TransferDTO(5, 10)   // Worse value/weight ratio
            );

            TransferRequestDTO request = new TransferRequestDTO(5, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(20, result.getTotalCost());
            assertEquals(5, result.getTotalWeight());
            assertEquals(1, result.getSelectedTransfers().size());
            assertEquals(20, result.getSelectedTransfers().get(0).getCost());
        }

        @Test
        @DisplayName("Should maximize total value within weight constraint")
        void testValueMaximization() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(2, 3),  // Value/weight = 1.5
                    new TransferDTO(3, 4),  // Value/weight ≈ 1.33
                    new TransferDTO(4, 8),  // Value/weight = 2
                    new TransferDTO(5, 7)   // Value/weight = 1.4
            );

            TransferRequestDTO request = new TransferRequestDTO(9, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(15, result.getTotalCost());  // Should select weight 4 (cost 8) and weight 5 (cost 7)
            assertTrue(result.getTotalWeight() <= 9);
        }

        @Test
        @DisplayName("Should handle multiple optimal solutions")
        void testMultipleOptimalSolutions() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(5, 10),
                    new TransferDTO(5, 10),
                    new TransferDTO(5, 10)
            );

            TransferRequestDTO request = new TransferRequestDTO(10, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(20, result.getTotalCost());
            assertEquals(10, result.getTotalWeight());
            assertEquals(2, result.getSelectedTransfers().size());
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of transfers efficiently")
        void testLargeNumberOfTransfers() {
            List<TransferDTO> transfers = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                transfers.add(new TransferDTO(1 + i % 5, 2 + i % 10));
            }

            long startTime = System.currentTimeMillis();
            TransferRequestDTO request = new TransferRequestDTO(50, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);
            long endTime = System.currentTimeMillis();

            assertNotNull(result);
            assertTrue(result.getTotalWeight() <= 50);
            assertTrue(endTime - startTime < 1000, "Processing time should be less than 1 second");
        }
    }

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidationTests {

        @Test
        @DisplayName("Should handle negative weights")
        void testNegativeWeights() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(-5, 10),
                    new TransferDTO(5, 10)
            );

            TransferRequestDTO request = new TransferRequestDTO(10, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(10, result.getTotalCost());
            assertEquals(5, result.getTotalWeight());
            assertEquals(1, result.getSelectedTransfers().size());
        }

        @Test
        @DisplayName("Should handle negative costs")
        void testNegativeCosts() {
            List<TransferDTO> transfers = Arrays.asList(
                    new TransferDTO(5, -10),
                    new TransferDTO(5, 10)
            );

            TransferRequestDTO request = new TransferRequestDTO(10, transfers);
            TransferResponseDTO result = transferService.findOptimalTransfers(request);

            assertEquals(10, result.getTotalCost());
            assertEquals(5, result.getTotalWeight());
            assertEquals(1, result.getSelectedTransfers().size());
        }
    }
}
