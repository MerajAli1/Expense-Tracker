package com.businesstracker.backend;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends MongoRepository<Userdetail, String> {
    Optional<Userdetail> findByUsername(String username);
}
