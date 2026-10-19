package com.example.familyhealth.service;

import com.example.familyhealth.mapper.StudentMapper;
import com.example.familyhealth.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Studentservice {
    @Autowired
    StudentMapper studentMapper;

    public  void addStudent (int no,String name ,String gender , String brithday,double height ,int classid){
        System.out.println(no);
        Student student = new Student(no,name,gender,brithday,height,classid);
        studentMapper.addStudent(student);
    }
    public  void  deleteStudent(int no){

        studentMapper.deleteByNo(no);
        System.out.println("已经删除");
    }

    public Student searchStudent(int delNO) {
        Student student =studentMapper.searchStudent(delNO);
        return student;

    }
}

