package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.Test;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends CrudRepository<Test, Integer> {
    @Override
    Optional<Test> findById(Integer integer);

    @Override
    Iterable<Test> findAll();
}
