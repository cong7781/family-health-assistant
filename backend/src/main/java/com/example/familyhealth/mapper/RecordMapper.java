package com.example.familyhealth.mapper;

import com.example.familyhealth.model.Record;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecordMapper {

    /**
     * 分页查询当前登录用户的健康记录（按记录时间倒序）。
     */
    List<Record> recordList(@Param("loginMemberId") Integer loginMemberId,
                            @Param("offset") int offset,
                            @Param("size") int size);

    /** 当前登录用户的记录总数 */
    long countRecords(@Param("loginMemberId") Integer loginMemberId);

    Record recordGetById(@Param("id") Integer id, @Param("loginMemberId") Integer loginMemberId);

    void recordSaveRecord(Record record);

    /** @return 受影响行数；为 0 说明 id 不存在或不属于当前用户 */
    int recordUpdateRecord(@Param("r") Record record, @Param("loginMemberId") Integer loginMemberId);

    /** @return 受影响行数；为 0 说明 id 不存在或不属于当前用户 */
    int recordDeleteById(@Param("id") Integer id, @Param("loginMemberId") Integer loginMemberId);
}
