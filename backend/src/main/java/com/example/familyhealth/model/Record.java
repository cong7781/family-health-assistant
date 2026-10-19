package com.example.familyhealth.model;

import java.util.Date;

public class Record {
    private Integer id;
    private Double temperature;    // 体温
    private Integer bpHigh;       // 高压
    private Integer bpLow;        // 低压
    private Integer bloodOxygen;  // 血氧
    private Double weight;        // 体重
    private Double bloodSugar;    // 血糖
    private String remark;        // 备注
    private Integer memberid;     // 关联家人id
    private String memberName;    // 联表查询姓名，页面展示用
    private Date createTime;      // 创建时间

    // getter setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getBpHigh() {
        return bpHigh;
    }

    public void setBpHigh(Integer bpHigh) {
        this.bpHigh = bpHigh;
    }

    public Integer getBpLow() {
        return bpLow;
    }

    public void setBpLow(Integer bpLow) {
        this.bpLow = bpLow;
    }

    public Integer getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(Integer bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Double bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getMemberid() {
        return memberid;
    }

    public void setMemberid(Integer memberid) {
        this.memberid = memberid;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}