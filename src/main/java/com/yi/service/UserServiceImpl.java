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
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void login(String openId, String name, String avatarUrl) {
        System.out.println("------------start login (UserServiceImpl)-----");
//        userDao.register("openId","name","avatarUrl");
        userDao.register(openId, name, avatarUrl);
     /*   int id = userDao.getIdByOpenId(openId);
        System.out.println("id=**  "+id);
        if (id <= 0) {
            userDao.register(openId, name, avatarUrl);
        } else {
            userDao.updata(openId, name, avatarUrl);
        }*/

    }
}
