package org.example.repository;

import org.example.entity.OrderAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAllocationRepository extends JpaRepository<OrderAllocation, Long> {}
