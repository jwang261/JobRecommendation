package com.jwang261.service;

import com.jwang261.dao.UserDao;
import com.jwang261.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public String getFullName(String userId) {
        User user = userDao.selectUserByUserId(userId);
        return user.getFirstName() + user.getLastName();
    }

    @Override
    public boolean verifyLogin(String userId, String password) {
        return null != userDao.selectUserByUserIdAndPassword(userId, password);
    }

    @Override
    public boolean addUser(User user) {
        return userDao.saveUser(user) == 1;
    }
}
