package com.treasury.treasuryhub.service;

import com.treasury.treasuryhub.dto.FeedbackDto;
import com.treasury.treasuryhub.exception.FeedbackNotFoundException;
import com.treasury.treasuryhub.model.Feedback;
import com.treasury.treasuryhub.model.User;
import com.treasury.treasuryhub.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserService userService;

    public Feedback registerFeedback(FeedbackDto feedbackDto) {
        User user = userService.fetchCurrentUser();
        Feedback feedback = new Feedback();
        feedback.setUserId(user.getId());
        feedback.setRating(feedbackDto.getRating());
        feedback.setFrequency(feedbackDto.getFrequency());
        feedback.setComment(feedbackDto.getComment());

//        System.out.println(feedbackDto.getFavouriteFunctionalities());

        return feedbackRepository.save(feedback);
    }

    public Feedback getFeedbackById(int id) throws FeedbackNotFoundException {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
    }

    public List<Feedback> getFeedbacksByUser() {
        User user = userService.fetchCurrentUser();
        return feedbackRepository.getFeedbackByUserId(user.getId());
    }
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback updateFeedback(FeedbackDto feedbackDto, int id) {
        return feedbackRepository.findById(id)
                .map(feedback -> {
                    feedback.setRating(feedbackDto.getRating());
                    feedback.setFrequency(feedbackDto.getFrequency());
                    feedback.setComment(feedbackDto.getComment());
                    return feedbackRepository.save(feedback);
                })
                .orElseGet(() -> registerFeedback(feedbackDto));
    }

    public void deleteFeedback(int id) throws FeedbackNotFoundException {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
        feedbackRepository.delete(feedback);
    }
}
