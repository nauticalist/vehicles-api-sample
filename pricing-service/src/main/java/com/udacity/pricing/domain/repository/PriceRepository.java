package com.udacity.pricing.domain.repository;

import com.udacity.pricing.domain.model.Price;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository  extends PagingAndSortingRepository<Price, Long> {

}
