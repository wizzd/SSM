package com.yi.service;

import com.yi.dao.UserDao;
import com.yi.entity.UserDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Demo class
 *
 * @author keriezhang
 * @date 2016/10/31
 */
@Service
public class UserServiceImpl implements UserService{
    private UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    public  UserDo login(String name,String password){
        UserDo userDo = userDao.getUser(name,password);
        if  (userDao == null) {
            return null;
        } else {
            return userDo;
        }
    }
    public void updateUserInfo(UserDo oldUser,UserDo newUser) {
        userDao.updateUserInfo(oldUser,newUser);
    }
    public void signUp(UserDo userDo){
        userDao.addUser(userDo);
    }

}
