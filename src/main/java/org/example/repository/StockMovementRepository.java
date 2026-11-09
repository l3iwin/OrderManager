package org.example.repository;

import org.example.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT sm FROM StockMovement sm WHERE sm.item.id = :itemId AND sm.quantityAvailable > 0 ORDER BY sm.creationDate ASC, sm.id ASC")
    List<StockMovement> findAvailableByItemIdWithLock(@Param("itemId") Long itemId);
}
