package com.example.one.repository;

import com.example.one.entity.CardBin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardbinRepository extends JpaRepository<CardBin, Integer> {
    
}
