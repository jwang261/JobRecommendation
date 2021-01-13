package com.jwang261.dao;

import com.jwang261.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Mapper
@Component
public interface ItemDao {
    public void saveItem(Item item);

    public Item selectItemByItemId(String itemId);


}
