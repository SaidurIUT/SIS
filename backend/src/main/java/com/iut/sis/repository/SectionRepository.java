package com.iut.sis.repository;

import com.iut.sis.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    List<Section> findByCourseId(int courseId);
    List<Section> findBySemesterId(int semesterId);
    Section findBySectionNumberAndCourseIdAndSemesterId(int sectionNumber, int courseId, int semesterId);
}