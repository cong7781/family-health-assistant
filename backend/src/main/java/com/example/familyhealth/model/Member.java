package com.example.familyhealth.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * 家庭成员实体类（数据库 member 表的映射）
 * 字段名与数据库列名对应(MyBatis的map-underscore-to-camel-case已开启)
 * 注意：数据库列名PASSWORD是大写，SQL中用 AS password 别名映射到本字段
 */
public class Member {
    private Integer id;      // 主键，对应数据库 id
    private String account;  // 登录账号，对应数据库 account
    private String password; // 登录密码，对应数据库 PASSWORD（大写别名映射）
    private String name;
    private String gender;
    private String phone;
    private Date birthday;
    private String bloodType;


    public String getIllnessHistory() {
        return illnessHistory;
    }

    public void setIllnessHistory(String illnessHistory) {
        this.illnessHistory = illnessHistory;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private String illnessHistory;
    private String allergy;
    private Date createTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private  String token;

    // 无参构造
    public Member() {}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 密码字段永远不序列化给前端：为 null 时整个字段不出现，
     * 这样「密码与哈希不出现在任何接口响应中」才是真的。
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}