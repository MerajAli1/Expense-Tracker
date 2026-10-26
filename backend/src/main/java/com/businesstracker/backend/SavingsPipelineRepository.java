package com.businesstracker.backend;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsPipelineRepository extends MongoRepository<SavingsPipelines, String> {

    List<SavingsPipelines> findAllByOrderByDateBorrowedDesc();

    List<SavingsPipelines> findByIsReturnedOrderByDateBorrowedDesc(boolean isReturned);
}
