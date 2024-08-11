package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.ReviewRequest;
import org.springframework.http.ResponseEntity;

public interface IReviewService {
    ResponseEntity<?> createReview(ReviewRequest request);

    ResponseEntity<?> getReviewsByTripId(Long tripId);
}
