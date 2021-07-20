package com.zjut.student_outside.repository;

import com.zjut.student_outside.model.Office;
import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.model.VolActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long>
{
    Page<Office> findAllByTime(Pageable pageable, String time);

    @Query(value = "select count(*) from office where time=?1 and student_id in (select id from student where banji=?2)", nativeQuery
            = true)
    Integer countOfficeForBanji(String time, String clazzName);

    @Query(value = "select count(*) from office where time=?1 and student_id in (select id from student where grade=?2)", nativeQuery
            = true)
    Integer countOfficeForGrade(String time, String gradeName);

    Page<Office> findAllByStudent(Student stu, Pageable pageable);

    @Transactional
    void deleteAllByStudent(Student student);
}
