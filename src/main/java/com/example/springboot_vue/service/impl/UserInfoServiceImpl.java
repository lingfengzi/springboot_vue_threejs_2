package com.example.springboot_vue.service.impl;

import com.example.springboot_vue.mapper.UserInfoMapper;
import com.example.springboot_vue.model.UserInfo;
import com.example.springboot_vue.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserInfo loginCheck(String username, String password) {
        return userInfoMapper.loginCheck(username,password);
    }

    @Override
    public int updateUserToken(String username, String token) {
        return userInfoMapper.updateUserToken(username,token);
    }

    @Override
    public UserInfo selectByToken(String token) {
        return userInfoMapper.selectByToken(token);
    }
}
