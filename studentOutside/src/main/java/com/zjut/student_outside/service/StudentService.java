package com.zjut.student_outside.service;

import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.repository.OfficeRepository;
import com.zjut.student_outside.repository.StudentRepository;
import com.zjut.student_outside.repository.VolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService
{
    @Autowired
    StudentRepository studentRep;

    @Autowired
    OfficeRepository officeRepo;

    @Autowired
    VolRepository volRepo;

    public Page<Student> findAll(Integer pageno, Integer size)
    {
        Pageable pageable = PageRequest.of(pageno, size);
        return studentRep.findAll(pageable);
    }

    public Student getStuById(Long id)
    {
        return studentRep.getById(id);
    }

    public List<Map<String, String>> findAllGradeAndBanji(String cond)
    {
        List<String> tmp = null;
        switch (cond) {
            case "grade":
                tmp = studentRep.findAllGrade();
                break;
            case "banji":
                tmp = studentRep.findAllBanji();
        }
        List<Map<String, String>> List = new ArrayList<>();
        for (String str : tmp) {
            Map<String, String> map = new HashMap<>();
            map.put("text", str);
            map.put("value", str);
            List.add(map);
        }
        return List;
    }

    public Student updateStu(Student student)
    {
        Student stu = studentRep.findById(student.getId()).orElseThrow();
        student.setOffices(stu.getOffices());
        student.setVols(stu.getVols());
        return studentRep.save(student);
    }

    public Student findById(long id)
    {
        return studentRep.getStuById(id);
    }

    public void delStu(Integer id)
    {
        Student stu = studentRep.findById((long) id).orElseThrow();
        stu.setOffices(null);
        officeRepo.deleteAllByStudent(stu);
        stu.setVols(null);
        studentRep.save(stu);
        studentRep.deleteById((long) id);
    }
}
