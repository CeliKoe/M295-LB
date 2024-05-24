package com.m295.lb.repos;

import com.m295.lb.models.Lending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LendingRepository extends JpaRepository<Lending, Integer> {

    Optional<Lending> findById(Integer lendingId);
}
