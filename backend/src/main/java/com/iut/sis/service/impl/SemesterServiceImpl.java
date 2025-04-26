package com.iut.sis.service.impl;

import com.iut.sis.entity.EducationalSession;
import com.iut.sis.entity.Semester;
import com.iut.sis.exception.ApiException;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.SemesterDto;
import com.iut.sis.repository.EducationalSessionRepository;
import com.iut.sis.repository.SemesterRepository;
import com.iut.sis.service.SemesterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private EducationalSessionRepository sessionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SemesterDto createSemester(SemesterDto semesterDto) {
        Semester semester = this.modelMapper.map(semesterDto, Semester.class);

        // Get session if provided
        if (semesterDto.getSession() != null && semesterDto.getSession().getId() != 0) {
            EducationalSession session = this.sessionRepository.findById(semesterDto.getSession().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Educational Session", "Id", semesterDto.getSession().getId()));
            semester.setSession(session);
        }

        // If this is set as current, clear other current semesters
        if (semester.isCurrent()) {
            this.clearCurrentSemesters();
        }

        Semester savedSemester = this.semesterRepository.save(semester);
        return this.modelMapper.map(savedSemester, SemesterDto.class);
    }

    @Override
    public SemesterDto updateSemester(SemesterDto semesterDto, Integer semesterId) {
        Semester semester = this.semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "Id", semesterId));

        semester.setName(semesterDto.getName());
        semester.setSemesterNumber(semesterDto.getSemesterNumber());
        semester.setStartDate(semesterDto.getStartDate());
        semester.setEndDate(semesterDto.getEndDate());

        // Update session if provided
        if (semesterDto.getSession() != null && semesterDto.getSession().getId() != 0) {
            EducationalSession session = this.sessionRepository.findById(semesterDto.getSession().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Educational Session", "Id", semesterDto.getSession().getId()));
            semester.setSession(session);
        }

        // If this is set as current, clear other current semesters
        if (semesterDto.isCurrent() && !semester.isCurrent()) {
            this.clearCurrentSemesters();
            semester.setCurrent(true);
        } else {
            semester.setCurrent(semesterDto.isCurrent());
        }

        Semester updatedSemester = this.semesterRepository.save(semester);
        return this.modelMapper.map(updatedSemester, SemesterDto.class);
    }

    @Override
    public SemesterDto getSemesterById(Integer semesterId) {
        Semester semester = this.semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "Id", semesterId));
        return this.modelMapper.map(semester, SemesterDto.class);
    }

    @Override
    public SemesterDto getCurrentSemester() {
        Semester currentSemester = this.semesterRepository.findByIsCurrent(true)
                .orElseThrow(() -> new ApiException("No current semester found"));
        return this.modelMapper.map(currentSemester, SemesterDto.class);
    }

    @Override
    public List<SemesterDto> getAllSemesters() {
        List<Semester> semesters = this.semesterRepository.findAll();
        return semesters.stream()
                .map(semester -> this.modelMapper.map(semester, SemesterDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SemesterDto> getSemestersBySession(Integer sessionId) {
        List<Semester> semesters = this.semesterRepository.findBySessionId(sessionId);
        return semesters.stream()
                .map(semester -> this.modelMapper.map(semester, SemesterDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSemester(Integer semesterId) {
        Semester semester = this.semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "Id", semesterId));
        this.semesterRepository.delete(semester);
    }

    @Override
    public void setCurrentSemester(Integer semesterId) {
        // Clear all current semesters first
        this.clearCurrentSemesters();

        // Set the specified semester as current
        Semester semester = this.semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "Id", semesterId));
        semester.setCurrent(true);
        this.semesterRepository.save(semester);
    }

    private void clearCurrentSemesters() {
        List<Semester> currentSemesters = this.semesterRepository.findAllByIsCurrent(true);
        if (!currentSemesters.isEmpty()) {
            currentSemesters.forEach(s -> s.setCurrent(false));
            this.semesterRepository.saveAll(currentSemesters);
        }
    }
}