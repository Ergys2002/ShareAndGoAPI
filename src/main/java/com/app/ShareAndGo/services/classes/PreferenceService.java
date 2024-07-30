package com.app.ShareAndGo.services.classes;

import com.app.ShareAndGo.dto.requests.PreferenceRequest;
import com.app.ShareAndGo.entities.Preference;
import com.app.ShareAndGo.entities.Trip;
import com.app.ShareAndGo.entities.TripPreference;
import com.app.ShareAndGo.entities.User;
import com.app.ShareAndGo.repositories.PreferenceRepository;
import com.app.ShareAndGo.repositories.TripPreferenceRepository;
import com.app.ShareAndGo.repositories.TripRepository;
import com.app.ShareAndGo.services.interfaces.IPreferenceService;
import com.app.ShareAndGo.services.interfaces.ITripService;
import com.app.ShareAndGo.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PreferenceService implements IPreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final ITripService tripService;
    private final TripRepository tripRepository;
    private final TripPreferenceRepository tripPreferenceRepository;
    @Override
    public ResponseEntity<?> handleChosenPreferences(List<PreferenceRequest> preferences) {
        if(!preferences.isEmpty()) {
            List<Preference> existingPreferences = preferenceRepository.findAll();
            Trip latestTrip = tripService.getLatestTripOfAuthenticatedUser();

            if (latestTrip != null) {
                    Set<String> preferencesTitles = preferences.stream()
                            .map(PreferenceRequest::getTitle)
                            .collect(Collectors.toSet());

                    preferences.forEach(preferenceRequest -> {
                        String title = preferenceRequest.getTitle().toLowerCase();
                        Preference preference = existingPreferences.stream()
                                .filter(p -> p.getTitle().equals(title))
                                .findFirst()
                                .orElse(Preference.builder()
                                        .title(title)
                                        .preferenceUsage(0)
                                        .build());

                        preference.setPreferenceUsage(preference.getPreferenceUsage() + 1);
                        preferenceRepository.save(preference);
                        existingPreferences.add(preference);
                    });

                    Set<TripPreference> tripPreferences = preferences.stream()
                            .map(preferenceRequest -> {
                                String title = preferenceRequest.getTitle().toLowerCase();
                                Preference preference = existingPreferences.stream()
                                        .filter(p -> p.getTitle().equals(title))
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException("Preference should exist at this point"));

                                return TripPreference.builder()
                                        .preference(preference)
                                        .trip(latestTrip)
                                        .build();
                            })
                            .collect(Collectors.toSet());

//                    latestTrip.setTripPreferences(tripPreferences);
//                    tripRepository.save(latestTrip);
                    tripPreferenceRepository.saveAll(tripPreferences);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Udhetimi per te cilin po zgjidhni preferencat nuk ekziston");
            }
        }

        return ResponseEntity.ok("Preferencat per udhetimin tuaj u ruajten me sukses");
    }
}
