package com.zjut.student_outside.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zjut.student_outside.model.Office;
import com.zjut.student_outside.model.Student;
import com.zjut.student_outside.model.VolActivity;
import com.zjut.student_outside.service.OfficeService;
import com.zjut.student_outside.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OfficeController
{
    @Autowired
    OfficeService officeSer;

    @Autowired
    StudentService stuSer;

    @GetMapping("/teacher/office/findAllByYear")
    @ResponseBody
    public Object findAllByYear(String year, Integer pageno, Integer size)
    {
        return officeSer.findAllByYear(year, pageno, size);
    }

    @GetMapping("/teacher/office/loadPieData")
    @ResponseBody
    public Object loadPieData(String time)
    {
        Map<String, Object> ClazzData = officeSer.loadClazzOffice(time);
        Map<String, Object> GradeData = officeSer.loadGradeOffice(time);
        // 处理tooltip
        List<String> labelList = (List<String>) ClazzData.get("opinion");
        labelList.addAll((List<String>) GradeData.get("opinion"));

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("opinion", labelList);
        dataMap.put("clazzData", ClazzData.get("opinionData"));
        dataMap.put("gradeData", GradeData.get("opinionData"));
        return dataMap;
    }

    @GetMapping("/office/details")
    @ResponseBody
    public Object details(Long id, Integer pageno, Integer size)
    {
        Map<String, Object> dataMap = new HashMap<>();
        Student stu = stuSer.getStuById(id);
        dataMap.put("office", officeSer.findOfficePage(stu, pageno, size));
        return dataMap;
    }

    @RequestMapping("/admin/office/addOffice")
    @ResponseBody
    public Object addVol(@RequestBody String domains, @RequestBody String stuName)
    {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(domains);
        String[] nameList = stuName.split(",");
        officeSer.addOffice(jsonObject, nameList);
        return null;
    }

    @GetMapping("/admin/office/del")
    public ModelAndView delete(Integer id, Integer pageno, Integer size)
    {
        long stuId = officeSer.delOffice(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("id", stuId);
        mv.addObject("pageno", pageno);
        mv.addObject("size", size);
        mv.setViewName("forward:/office/details");
        return mv;
    }

    @PostMapping("/admin/office/update")
    @ResponseBody
    public String updateOfficeById(Office office)
    {
        Office o = officeSer.updateOfficeById(office);
        if (o == null) {
            return "failed";
        }
        return "success";
    }
}