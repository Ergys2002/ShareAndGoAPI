package com.app.ShareAndGo.controllers;

import com.app.ShareAndGo.dto.requests.PreferenceRequest;
import com.app.ShareAndGo.services.interfaces.IPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preference")
@RequiredArgsConstructor
public class PreferenceController {

    private final IPreferenceService preferenceService;

    @PostMapping("/choose-preferences")
    public ResponseEntity<?> chosePreferences(@RequestBody List<PreferenceRequest> chosenPreferences){
        return preferenceService.handleChosenPreferences(chosenPreferences);
    }

    @GetMapping("/preferences-by-trip-id")
    public ResponseEntity<?> getPreferencesByTripId(@RequestParam("id") Long id){
        return preferenceService.getPreferencesByTripId(id);
    }
}
