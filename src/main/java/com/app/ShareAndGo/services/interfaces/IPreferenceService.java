package com.app.ShareAndGo.services.interfaces;

import com.app.ShareAndGo.dto.requests.PreferenceRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPreferenceService {
    ResponseEntity<?> handleChosenPreferences(List<PreferenceRequest> preferences);

    ResponseEntity<?> getPreferencesByTripId(Long id);
}
