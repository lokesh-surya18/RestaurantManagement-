package com.example.restaurant.repo;

import com.example.restaurant.model.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingRequest, Long> {
    List<BookingRequest> findByBranchId(Long branchId);
}