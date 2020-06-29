package com.example.springboot_vue.service;

import com.example.springboot_vue.model.UserInfo;

public interface UserInfoService {
    UserInfo loginCheck(String username, String password);

    int updateUserToken(String username,String token);

    UserInfo selectByToken(String token);
}
