package com.ms.awe.musicoder.login.model;

/**
 * Created by awe on 2018/5/17.
 */

public class UserModel implements IUser {

    String name;
    String passwd;

    public UserModel(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPasswd() {
        return passwd;
    }

    /**
     * 检测用户名密码输入规则
     */
    @Override
    public int checkUserValidity(String name, String passwd) {
        //判断用户为空、密码为空、用户名正确、密码正确
        if (name == null || passwd == null || !name.equals(getName()) || !passwd.equals(getPasswd())) {
            return -1;
        }
        return 0;
    }
}
