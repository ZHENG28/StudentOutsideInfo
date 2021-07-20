package com.zjut.student_outside.controller;

import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class StudentController
{
    @Autowired
    StudentService stuSer;

    @PostMapping("/student/findAllGradeAndBanji")
    @ResponseBody
    public Object findAllGradeAndBanji()
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("gradeArr", stuSer.findAllGradeAndBanji("grade"));
        dataMap.put("banjiArr", stuSer.findAllGradeAndBanji("banji"));
        return dataMap;
    }

    @PostMapping("/admin/student/findNameById")
    @ResponseBody
    public Object findNameByStuId(long id)
    {
        return stuSer.findById(id);
    }

    @PostMapping("/admin/student/update")
    @ResponseBody
    public String updateStu(Student student)
    {
        Student stu = stuSer.updateStu(student);
        if (stu == null) {
            return "failed";
        }
        return "success";
    }

    @GetMapping("/admin/student/del")
    @ResponseBody
    public Object updateStu(Integer id, Integer pageno, Integer size)
    {
        stuSer.delStu(id);
        return stuSer.findAll(pageno, size);
    }
}
