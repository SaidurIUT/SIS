package com.iut.sis.service;

import com.iut.sis.payloads.EducationalSessionDto;
import java.util.List;

public interface EducationalSessionService {
    EducationalSessionDto createSession(EducationalSessionDto sessionDto);
    EducationalSessionDto updateSession(EducationalSessionDto sessionDto, Integer sessionId);
    EducationalSessionDto getSessionById(Integer sessionId);
    EducationalSessionDto getCurrentSession();
    List<EducationalSessionDto> getAllSessions();
    void deleteSession(Integer sessionId);
    void setCurrentSession(Integer sessionId);
}