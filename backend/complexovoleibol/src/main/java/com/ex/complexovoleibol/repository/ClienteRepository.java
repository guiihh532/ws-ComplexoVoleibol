package com.ex.complexovoleibol.repository;

import com.ex.complexovoleibol.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, Long> {

}