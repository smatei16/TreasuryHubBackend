package com.treasury.treasuryhub.controller;

import com.treasury.treasuryhub.dto.FeedbackDto;
import com.treasury.treasuryhub.exception.FeedbackNotFoundException;
import com.treasury.treasuryhub.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/save")
    public ResponseEntity<?> registerFeedback(@RequestBody FeedbackDto feedbackDto) {
        return new ResponseEntity<>(feedbackService.registerFeedback(feedbackDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable int id) throws FeedbackNotFoundException {
        return new ResponseEntity<>(feedbackService.getFeedbackById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFeedback() {
        return new ResponseEntity<>(feedbackService.getAllFeedbacks(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeedback(@RequestBody FeedbackDto feedbackDto, @PathVariable int id) {
        return new ResponseEntity<>(feedbackService.updateFeedback(feedbackDto, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable int id) throws FeedbackNotFoundException {
        feedbackService.deleteFeedback(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
