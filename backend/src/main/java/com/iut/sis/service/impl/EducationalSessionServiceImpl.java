package com.iut.sis.service.impl;

import com.iut.sis.entity.EducationalSession;
import com.iut.sis.exception.ApiException;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.EducationalSessionDto;
import com.iut.sis.repository.EducationalSessionRepository;
import com.iut.sis.service.EducationalSessionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationalSessionServiceImpl implements EducationalSessionService {

    @Autowired
    private EducationalSessionRepository sessionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public EducationalSessionDto createSession(EducationalSessionDto sessionDto) {
        EducationalSession session = this.modelMapper.map(sessionDto, EducationalSession.class);

        // If this is set as current, clear other current sessions
        if (session.isCurrent()) {
            this.clearCurrentSessions();
        }

        EducationalSession savedSession = this.sessionRepository.save(session);
        return this.modelMapper.map(savedSession, EducationalSessionDto.class);
    }

    @Override
    public EducationalSessionDto updateSession(EducationalSessionDto sessionDto, Integer sessionId) {
        EducationalSession session = this.sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Educational Session", "Id", sessionId));

        session.setName(sessionDto.getName());
        session.setStartDate(sessionDto.getStartDate());
        session.setEndDate(sessionDto.getEndDate());

        // If this is set as current, clear other current sessions
        if (sessionDto.isCurrent() && !session.isCurrent()) {
            this.clearCurrentSessions();
            session.setCurrent(true);
        } else {
            session.setCurrent(sessionDto.isCurrent());
        }

        EducationalSession updatedSession = this.sessionRepository.save(session);
        return this.modelMapper.map(updatedSession, EducationalSessionDto.class);
    }

    @Override
    public EducationalSessionDto getSessionById(Integer sessionId) {
        EducationalSession session = this.sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Educational Session", "Id", sessionId));
        return this.modelMapper.map(session, EducationalSessionDto.class);
    }

    @Override
    public EducationalSessionDto getCurrentSession() {
        EducationalSession currentSession = this.sessionRepository.findByIsCurrent(true)
                .orElseThrow(() -> new ApiException("No current educational session found"));
        return this.modelMapper.map(currentSession, EducationalSessionDto.class);
    }

    @Override
    public List<EducationalSessionDto> getAllSessions() {
        List<EducationalSession> sessions = this.sessionRepository.findAll();
        return sessions.stream()
                .map(session -> this.modelMapper.map(session, EducationalSessionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSession(Integer sessionId) {
        EducationalSession session = this.sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Educational Session", "Id", sessionId));
        this.sessionRepository.delete(session);
    }

    @Override
    public void setCurrentSession(Integer sessionId) {
        // Clear all current sessions first
        this.clearCurrentSessions();

        // Set the specified session as current
        EducationalSession session = this.sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Educational Session", "Id", sessionId));
        session.setCurrent(true);
        this.sessionRepository.save(session);
    }

    private void clearCurrentSessions() {
        List<EducationalSession> currentSessions = this.sessionRepository.findAllByIsCurrent(true);
        if (!currentSessions.isEmpty()) {
            currentSessions.forEach(s -> s.setCurrent(false));
            this.sessionRepository.saveAll(currentSessions);
        }
    }
}