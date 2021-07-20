package com.zjut.student_outside.controller;

import com.alibaba.fastjson.JSONObject;
import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.model.VolActivity;
import com.zjut.student_outside.service.StudentService;
import com.zjut.student_outside.service.VolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VolController
{
    @Autowired
    VolService volSer;

    @Autowired
    StudentService stuSer;

    @GetMapping("/volunteer/findAllStu")
    @ResponseBody
    public Object findAllStudent(Integer pageno, Integer size)
    {
        return stuSer.findAll(pageno, size);
    }

    @GetMapping("/volunteer/details")
    @ResponseBody
    public Object details(Long id, Integer pageno, Integer size)
    {
        Map<String, Object> dataMap = new HashMap<>();
        Student stu = stuSer.getStuById(id);
        dataMap.put("volActs", volSer.findVolsPage(stu, pageno, size));
        dataMap.put("barData", volSer.loadStuVolData(id));
        return dataMap;
    }

    @GetMapping("/teacher/volunteer/loadPieData")
    @ResponseBody
    public Object loadPieData()
    {
        Map<String, Object> ClazzData = volSer.loadClazzVol();
        Map<String, Object> GradeData = volSer.loadGradeVol();
        // 处理tooltip
        List<String> labelList = (List<String>) ClazzData.get("opinion");
        labelList.addAll((List<String>) GradeData.get("opinion"));

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("opinion", labelList);
        dataMap.put("clazzData", ClazzData.get("opinionData"));
        dataMap.put("gradeData", GradeData.get("opinionData"));
        return dataMap;
    }

    @GetMapping("/teacher/volunteer/loadStackBarData")
    @ResponseBody
    public Object loadStackBarData(String grade)
    {
        List<String> labelList = volSer.findBanjiByGrade(grade);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("stackLabel", labelList);
        dataMap.put("stackbarSeries", volSer.loadStackBarData(grade));
        return dataMap;
    }

    @RequestMapping("/admin/volunteer/addVol")
    @ResponseBody
    public Object addVol(@RequestBody String domains)
    {
//        List<VolActivity> vols = JSONObject.parseArray(domains, VolActivity.class);
        System.out.println(JSONObject.parseArray(domains, VolActivity.class));
        return null;
    }

    @RequestMapping("/admin/volunteer/del")
    @ResponseBody
    public Object delete(Integer volId, Integer stuId, Integer pageno, Integer size)
    {
        volSer.delVol(volId, stuId);
        return details((long) stuId, pageno, size);
    }
}
