package com.yi.service;

import com.yi.entity.UserDo;

public interface UserService {
    UserDo login(String name, String password);
    void updateUserInfo(UserDo oldUser,UserDo newUser);
    void signUp(UserDo userDo);
}
