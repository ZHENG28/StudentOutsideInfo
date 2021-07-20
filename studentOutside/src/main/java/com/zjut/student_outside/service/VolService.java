package com.zjut.student_outside.service;

import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.model.VolActivity;
import com.zjut.student_outside.repository.StudentRepository;
import com.zjut.student_outside.repository.VolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

@Service
public class VolService
{
    @Autowired
    VolRepository volRep;

    @Autowired
    StudentRepository studentRep;

    public Map<String, Object> loadClazzVol()
    {
        Map<String, Object> pieMap = new HashMap<>();
        List<Map<String, Object>> pieData = new ArrayList<>();
        List<String> label = studentRep.findAllBanji();
        for (String clazzName : label) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", volRep.sumManhourForBanji(clazzName));
            map.put("name", clazzName);
            pieData.add(map);
        }
        pieMap.put("opinion", label);
        pieMap.put("opinionData", pieData);
        return pieMap;
    }

    public Map<String, Object> loadGradeVol()
    {
        Map<String, Object> pieMap = new HashMap<>();
        List<Map<String, Object>> pieData = new ArrayList<>();
        List<String> label = studentRep.findAllGrade();
        for (String gradeName : label) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", volRep.sumManhourForGrade(gradeName));
            map.put("name", gradeName);
            pieData.add(map);
        }
        pieMap.put("opinion", label);
        pieMap.put("opinionData", pieData);
        return pieMap;
    }

    public Object loadStuVolData(Long id)
    {
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        BigDecimal zero = new BigDecimal(0);
        List<BigDecimal> dataList = Arrays.asList(zero, zero, zero, zero, zero);

        List<Map<String, Object>> manhourList = volRep.sumManhourByTime(id);
        if (manhourList.size() != 0) {
            for (Map<String, Object> map : manhourList) {
                String year = String.valueOf(map.get("time")).substring(5);
                dataList.set(nowYear - Integer.parseInt(year), (BigDecimal) map.get("sum"));
            }
        }
        return dataList;
    }

    public List<String> findBanjiByGrade(String grade)
    {
        return studentRep.findBanjiByGradeLike("%" + grade.substring(2, 4));
    }

    public Object loadStackBarData(String grade)
    {
        List<Map<String, Object>> dataList = new ArrayList<>();

        List<String> banjiList = findBanjiByGrade(grade);
        for (String s : banjiList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", s);
            map.put("type", "bar");
            map.put("stack", "总工时");
            int[] data = new int[5];
            int nowYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 0; i < 5; i++) {
                String time = (nowYear - 1) + "-" + nowYear;
                data[i] = volRep.sumManhourForBanjiByTime(s, time);
                nowYear--;
            }
            System.out.println(Arrays.toString(data));
            map.put("data", data);
            dataList.add(map);
        }
        return dataList;
    }

    public Page<VolActivity> findVolsPage(Student stu, Integer pageno, Integer size)
    {
        Pageable pageable = PageRequest.of(pageno, size);
        return volRep.findAllByStudents(stu, pageable);
    }

    public void delVol(Integer volId, Integer stuId)
    {
        VolActivity vol = volRep.findById((long) volId).orElseThrow();
        Student stu = studentRep.findById((long) stuId).orElseThrow();
        vol.getStudents().remove(stu);
        stu.getVols().remove(vol);
        volRep.save(vol);
        studentRep.save(stu);
    }
}
