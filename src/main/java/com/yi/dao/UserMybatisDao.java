package com.yi.dao;

import com.yi.entity.UserDo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserMybatisDao  {
    private SqlSessionTemplate sessionTemplate;

    @Autowired
    public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }
    public UserDo getUser(String openId){
        UserDao userDao = sessionTemplate.getMapper(UserDao.class);
        return userDao.getUserByOpenId(openId);
    }
    public void updata(String openId,String name, String avatarUrl){
        UserDo user = getUser(openId);
        boolean flag = user.getName().equals(name) && user.getAvatarUrl().equals(avatarUrl);
        if (!flag){
            UserDao userDao = sessionTemplate.getMapper(UserDao.class);
            userDao.updata(openId,name,avatarUrl);
        }
    }
}
