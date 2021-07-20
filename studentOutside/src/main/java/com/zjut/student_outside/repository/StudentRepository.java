package com.zjut.student_outside.repository;

import com.zjut.student_outside.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>
{
    Student getStuById(Long id);

    Student findByName(String name);

    @Query("select distinct banji from Student")
    List<String> findAllBanji();

    @Query("select distinct grade from Student")
    List<String> findAllGrade();

    @Query("select distinct banji from Student where banji like ?1")
    List<String> findBanjiByGradeLike(String grade);
}
