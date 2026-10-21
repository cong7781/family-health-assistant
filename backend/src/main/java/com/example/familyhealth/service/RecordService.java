package com.example.familyhealth.service;

import com.example.familyhealth.commom.PageResult;
import com.example.familyhealth.mapper.RecordMapper;
import com.example.familyhealth.model.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    private final RecordMapper recordMapper;

    public RecordService(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }

    /**
     * 分页查询当前登录用户的健康记录。
     */
    public PageResult<Record> recordList(Integer loginMemberId, Integer pageParam, Integer sizeParam) {
        int page = PageResult.safePage(pageParam);
        int size = PageResult.safeSize(sizeParam);

        long total = recordMapper.countRecords(loginMemberId);
        List<Record> records = recordMapper.recordList(loginMemberId, (page - 1) * size, size);

        return new PageResult<>(total, page, size, records);
    }

    public Record recordGetById(Integer id, Integer loginMemberId) {
        return recordMapper.recordGetById(id, loginMemberId);
    }

    public void recordSaveRecord(Record record) {
        recordMapper.recordSaveRecord(record);
    }

    /** @return 受影响行数 */
    public int recordUpdateRecord(Record record, Integer loginMemberId) {
        return recordMapper.recordUpdateRecord(record, loginMemberId);
    }

    /** @return 受影响行数 */
    public int recordDeleteById(Integer id, Integer loginMemberId) {
        return recordMapper.recordDeleteById(id, loginMemberId);
    }
}
