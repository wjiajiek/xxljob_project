package com.xxl.job.executor.dao;

import com.xxl.job.executor.domain.RsTag;
import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wjj
 * @date 2023/11/13
 */
@Mapper
public interface RsTagDao {

    List<RsTag> selectRsTagAll();

    int insertRsTag(List<RsTag> rsTags);

}
