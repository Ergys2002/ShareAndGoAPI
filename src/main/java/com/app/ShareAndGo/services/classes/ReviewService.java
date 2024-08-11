package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.ReviewRequest;
import com.app.ShareAndGo.entities.*;
import com.app.ShareAndGo.enums.FeedbackType;
import com.app.ShareAndGo.repositories.ReviewRepository;
import com.app.ShareAndGo.repositories.TripFeedbackRepository;
import com.app.ShareAndGo.repositories.TripRepository;
import com.app.ShareAndGo.repositories.UserProfileRepository;
import com.app.ShareAndGo.services.interfaces.IReviewService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IUserService userService;
    private final ReviewRepository reviewRepository;
    private final TripRepository tripRepository;
    private final TripFeedbackRepository tripFeedbackRepository;
    private final UserProfileRepository userProfileRepository;


    @Override
    public ResponseEntity<?> createReview(ReviewRequest request) {
        User reviewer = userService.getAuthenticatedUser();

        if (request.getRecipientId().equals(reviewer.getId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ju nuk mund te lini nje pershtypje per veten tuaj");
        }
        Trip trip = tripRepository.findById(request.getTripId()).orElse(null);
        User recipient = userService.getUserById(request.getRecipientId());



        if (trip == null || recipient == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem me udhetimin ose marresin e pershtypjes");
        }

        TripFeedback tripFeedback = tripFeedbackRepository.findTripFeedbackByTripAndReviewerAndRecipientAndFeedbackType(trip,reviewer,recipient, FeedbackType.REVIEW);

        if(tripFeedback != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tashme keni lene pershtypje per kete perdorues");
        }

        Set<TripFeedback> reviewsRecipientHasReceived = tripFeedbackRepository.findTripFeedbacksByFeedbackTypeAndRecipient(FeedbackType.REVIEW, recipient);

        UserProfile recipientProfile = recipient.getProfile();
        if (recipient.getProfile().getRating() == 0 && reviewsRecipientHasReceived.isEmpty()){
            recipientProfile.setRating(request.getRating());
        }else {
            double calculatedRating = ((reviewsRecipientHasReceived.size() * recipientProfile.getRating()) + request.getRating()) / (reviewsRecipientHasReceived.size() + 1);
            recipientProfile.setRating(calculatedRating);
        }

        userProfileRepository.save(recipientProfile);

        Review review = Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .build();

        reviewRepository.save(review);

        TripFeedback feedback = TripFeedback.builder()
                .feedbackType(FeedbackType.REVIEW)
                .review(review)
                .reviewer(reviewer)
                .recipient(recipient)
                .trip(trip)
                .build();

        tripFeedbackRepository.save(feedback);

        return ResponseEntity.status(HttpStatus.OK).body("Pershtypja juaj u ruajt me sukses");
    }
}