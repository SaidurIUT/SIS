package com.iut.sis.repository;

import com.iut.sis.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Integer> {
    Optional<Semester> findByIsCurrent(boolean isCurrent);
    List<Semester> findBySessionId(int sessionId);
    Optional<Semester> findBySemesterNumberAndSessionId(int semesterNumber, int sessionId);
}