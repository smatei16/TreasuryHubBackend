package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.model.Test;
import com.treasury.treasuryhub.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public Test getTestById(int id){
        return testRepository.findById(id).orElse(null);
    }

    public Iterable<Test> getAllTest() {
        return testRepository.findAll();
    }

    public void saveTest() {
        Test newTest = new Test();
        newTest.setName("Matei");
        this.testRepository.save(newTest);
    }
}
