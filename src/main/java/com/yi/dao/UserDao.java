package com.yi.dao;


import com.yi.entity.UserDo;
public interface UserDao {
    void updata(String openId, String name,String avatarUrl);
    int getIdByOpenId(String openId);
    UserDo getUserByOpenId(String openId);
    void register(String openId, String name,String avatarUrl);
}
//ed breakpoint at com.sun.proxy.$Proxy13.toString()+0 because it happened inside debugger evaluation