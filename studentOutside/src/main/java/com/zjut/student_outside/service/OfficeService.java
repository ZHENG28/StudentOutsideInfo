package com.zjut.student_outside.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjut.student_outside.model.Office;
import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.repository.OfficeRepository;
import com.zjut.student_outside.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfficeService
{
    @Autowired
    StudentRepository studentRep;

    @Autowired
    OfficeRepository officeRep;

    public Page<Office> findAllByYear(String year, Integer pageno, Integer size)
    {
        Pageable pageable = PageRequest.of(pageno, size);
        return officeRep.findAllByTime(pageable, year);
    }

    public Map<String, Object> loadClazzOffice(String time)
    {
        Map<String, Object> pieMap = new HashMap<>();
        List<Map<String, Object>> pieData = new ArrayList<>();
        List<String> label = studentRep.findAllBanji();
        for (String clazzName : label) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", officeRep.countOfficeForBanji(time, clazzName));
            map.put("name", clazzName);
            pieData.add(map);
        }
        pieMap.put("opinion", label);
        pieMap.put("opinionData", pieData);
        return pieMap;
    }

    public Map<String, Object> loadGradeOffice(String time)
    {
        Map<String, Object> pieMap = new HashMap<>();
        List<Map<String, Object>> pieData = new ArrayList<>();
        List<String> label = studentRep.findAllGrade();
        for (String gradeName : label) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", officeRep.countOfficeForGrade(time, gradeName));
            map.put("name", gradeName);
            pieData.add(map);
        }
        pieMap.put("opinion", label);
        pieMap.put("opinionData", pieData);
        return pieMap;
    }

    public Page<Office> findOfficePage(Student stu, Integer pageno, Integer size)
    {
        Pageable pageable = PageRequest.of(pageno, size);
        return officeRep.findAllByStudent(stu, pageable);
    }

    public long delOffice(long id)
    {
        Office office = officeRep.findById(id).orElseThrow();
        office.getStudent().getOffices().remove(office);
        officeRep.delete(office);
        return office.getStudent().getId();
    }

    public Office updateOfficeById(Office office)
    {
        Office o = officeRep.getById(office.getId());
        office.setStudent(o.getStudent());
        return officeRep.save(office);
    }

    public void addOffice(JSONObject jsonObject, String[] nameList)
    {
//        for (JSONObject o : jsonArray) {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.putAll(jsonObject);
        List<Office> offices = JSON.parseArray((String) valueMap.get("domains"), Office.class);
//            Map map = JSON.parseObject((String) o, Map.class);
//            Student stu = studentRep.findByName((String) map.get("name"));
//            Office office = new Office((String) map.get("work"), (String) map.get("time"), stu);
//            stu.getOffices().add(office);
//            studentRep.save(stu);
//        }
    }
}
