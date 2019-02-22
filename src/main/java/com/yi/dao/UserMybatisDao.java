package com.yi.dao;

import com.yi.entity.UserDo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserMybatisDao {
    private SqlSessionTemplate sessionTemplate;

    @Autowired
    public void setSessionTemplate(SqlSessionTemplate sessionTemplate) {
        this.sessionTemplate = sessionTemplate;
    }
    public UserDo getUser(String name, String password){
        UserDao userDao = sessionTemplate.getMapper(UserDao.class);
        return userDao.getUser(name,password);
    }
}
