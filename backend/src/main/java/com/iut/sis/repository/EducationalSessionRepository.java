package com.iut.sis.repository;

import com.iut.sis.entity.EducationalSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EducationalSessionRepository extends JpaRepository<EducationalSession, Integer> {
    Optional<EducationalSession> findByIsCurrent(boolean isCurrent);
    Optional<EducationalSession> findByName(String name);
}