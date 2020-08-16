package com.udacity.pricing.domain.repository;

import com.udacity.pricing.domain.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository  extends JpaRepository<Price, Long> {

}
