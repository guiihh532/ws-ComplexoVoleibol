package com.ex.complexovoleibol.repository;

import com.ex.complexovoleibol.model.DatabaseSequence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseSequenceRepository extends MongoRepository<DatabaseSequence, String> {
}