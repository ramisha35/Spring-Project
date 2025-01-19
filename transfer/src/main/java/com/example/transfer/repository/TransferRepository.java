package com.example.transfer.repository;

import com.example.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByWeightLessThanEqual(int maxWeight);
    List<Transfer> findByOrderByCostDesc();
}