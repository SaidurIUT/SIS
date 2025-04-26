package com.iut.sis.repository;

import com.iut.sis.entity.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentInfoRepository extends JpaRepository<StudentInfo, String> {
    List<StudentInfo> findByDepartmentId(int departmentId);
    List<StudentInfo> findByCourseId(int courseId);
    List<StudentInfo> findByBatch(String batch);
    List<StudentInfo> findBySection(int section);
    List<StudentInfo> findByCurrentSemester(int semester);

    @Query("SELECT MAX(s.roll) FROM StudentInfo s WHERE s.batch = :batch AND s.department.id = :departmentId AND s.course.code = :courseCode AND s.section = :section")
    Optional<Integer> findMaxRollNumber(@Param("batch") String batch,
                                        @Param("departmentId") int departmentId,
                                        @Param("courseCode") int courseCode,
                                        @Param("section") int section);

    Optional<StudentInfo> findByUserId(int userId);
}