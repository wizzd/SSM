package com.yi.dao;


import com.yi.entity.UserDo;
public interface UserDao {
    UserDo getUser(String name, String password);
    void addUser(UserDo user);
    void updateUserInfo(UserDo oldUser, UserDo newUser);
}