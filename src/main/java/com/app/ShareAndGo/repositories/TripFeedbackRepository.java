package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.Review;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripFeedback;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.enums.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TripFeedbackRepository extends JpaRepository<TripFeedback, Long> {
    TripFeedback findTripFeedbackByTripAndReviewerAndRecipientAndFeedbackType(Trip trip, User reviewer, User recipient, FeedbackType feedbackType);

    Set<TripFeedback> findTripFeedbacksByFeedbackTypeAndRecipient(FeedbackType feedbackType, User recipient);

    Set<TripFeedback> findTripFeedbacksByFeedbackTypeAndTrip(FeedbackType feedbackType, Trip trip);
}
