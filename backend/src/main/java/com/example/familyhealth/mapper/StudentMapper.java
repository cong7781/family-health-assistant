package com.example.familyhealth.mapper;

import com.example.familyhealth.model.Student;

public interface StudentMapper {
    void  addStudent(Student student);
    void  deleteByNo(Integer no);
    Student searchStudent(int no);
}
