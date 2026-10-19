package com.example.familyhealth.model;

public class Student {
    private int no;
    private  String  name;
    private  String  gender;
    private  String  birthday;
    private  double height;
    private  int classid;

    public Student() {
    }

    public Student(int no, String name, String gender, String birthday, double height, int classid) {
        this.no = no;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.height = height;
        this.classid = classid;

    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }
    public  void addStudent (int no , String name , String gender, String birthday, double height, int classid){
        Student student = new Student (no,name,gender,birthday,height,classid);

    }
}
