package com.app.ShareAndGo.repositories;

import com.app.ShareAndGo.entities.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
}
