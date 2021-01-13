package com.jwang261.service;


import com.jwang261.pojo.User;

public interface UserService {
    public String getFullName(String userId);

    public boolean verifyLogin(String userId, String password);

    public boolean addUser(User user);
}
