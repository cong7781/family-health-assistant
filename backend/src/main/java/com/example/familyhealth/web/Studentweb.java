package com.example.familyhealth.web;

import com.example.familyhealth.service.Studentservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/student")
public class Studentweb {
    @Autowired
    Studentservice studentService;


    @RequestMapping(path = "/add")
    public String addStudent(){
        int no = 14;
        String name = "张三";
        String gender = "男";
        String brithday = "2005-09-08";
        double height = 1.77;
        int classid = 2;
        studentService.addStudent(no,name,gender,brithday,height,classid);
        return  "保存成功";
    }

    @RequestMapping(path = "/delete")
    public  String deleteStudent(){
        int delNO = 1;
        studentService.deleteStudent(delNO);
        return "删除成功";


    }
    @RequestMapping(path = "/search")
    public  String searchStudent() {
        int delNO = 9;
        studentService.searchStudent(delNO);
        return "查询成功";
    }
}
