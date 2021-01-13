package com.jwang261.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Mapper
@Component
public interface KeywordDao {
    public Set<String> selectKeywordByItemId(String itemId);
}
