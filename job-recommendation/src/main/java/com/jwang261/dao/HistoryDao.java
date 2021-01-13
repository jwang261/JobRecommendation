package com.jwang261.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper
@Component
public interface HistoryDao {



    public int unsetItem(@Param("itemId") String itemId, @Param("userId") String userId);

    //测试中
    public void setItem(@Param("itemId") String itemId, @Param("userId") String userId );

    public Set<String> selectItemIdByUserId(String userId);
}
