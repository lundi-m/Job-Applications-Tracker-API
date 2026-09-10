package com.lundim.job_applications_tracker.repository;

import com.lundim.job_applications_tracker.model.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

    Optional<CustomUser> findByEmail(String email);
}
