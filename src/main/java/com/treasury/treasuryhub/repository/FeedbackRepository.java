package com.treasury.treasuryhub.repository;

import com.treasury.treasuryhub.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    ArrayList<Feedback> getFeedbackByUserId(int id);
}
