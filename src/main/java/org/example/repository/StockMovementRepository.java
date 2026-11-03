package org.example.repository;

import org.example.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Query("SELECT sm FROM StockMovement sm WHERE sm.item.id = :itemId AND sm.quantityAvailable > 0 ORDER BY sm.creationDate ASC")
    List<StockMovement> findAvailableByItemId(@Param("itemId") Long itemId);
}
