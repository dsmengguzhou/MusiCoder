package com.ms.awe.musicoder.login.model;

/**
 * Created by awe on 2018/5/17.
 */

public interface IUser {

    String getName();

    String getPasswd();

    int checkUserValidity(String name ,String passwd);

}
