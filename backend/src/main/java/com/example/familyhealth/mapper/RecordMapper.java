package com.example.familyhealth.mapper;

import com.example.familyhealth.model.Record;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface RecordMapper {
    List<Record> recordList(Integer loginMemberId);

    Record recordGetById(Integer id, Integer loginMemberId);

    void recordSaveRecord(Record record);

    void recordUpdateRecord(@Param("r") Record record, @Param("loginMemberId") Integer loginMemberId);

    void recordDeleteById(@Param("id") Integer id, @Param("loginMemberId") Integer loginMemberId);
}