package com.jwang261.dao;

import com.jwang261.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDao {

    public User selectUserByUserId(String userId);

    public int saveUser(User user);

    public User selectUserByUserIdAndPassword(String userId, String password);
}
