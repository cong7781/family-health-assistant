package com.example.familyhealth.service;

import com.example.familyhealth.mapper.RecordMapper;
import com.example.familyhealth.model.Record;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class RecordService {
    @Resource
    private RecordMapper recordMapper;

    public List<Record> recordList(Integer loginMemberId) {
        return recordMapper.recordList(loginMemberId);
    }

    public Record recordGetById(Integer id, Integer loginMemberId) {
        return recordMapper.recordGetById(id, loginMemberId);
    }

    public void recordSaveRecord(Record record) {
        recordMapper.recordSaveRecord(record);
    }

    public void recordUpdateRecord(Record record, Integer loginMemberId) {
        recordMapper.recordUpdateRecord(record, loginMemberId);
    }

    public void recordDeleteById(Integer id, Integer loginMemberId) {
        recordMapper.recordDeleteById(id, loginMemberId);
    }
}