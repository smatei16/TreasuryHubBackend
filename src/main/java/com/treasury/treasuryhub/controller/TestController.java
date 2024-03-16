package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.model.Test;
import com.treasury.treasuryhub.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/getId")
    public Test getTestId(@RequestParam int id){
        return this.testService.getTestById(id);
    }

    @GetMapping("/getAll")
    public Iterable<Test> getTestAll(){
        return this.testService.getAllTest();
    }

    @PostMapping("/newTest")
    public void registerTest(){
        this.testService.saveTest();
    }
}
