package com.businesstracker.backend;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxVaultRepository extends MongoRepository<TaxLiabilityVault, String> {
}
