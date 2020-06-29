package com.example.springboot_vue.mapper;

import com.example.springboot_vue.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    /*验证登录，并取得用户信息*/
    UserInfo loginCheck(String username, String password);
    /*更新用户令牌*/
    int updateUserToken(String username,String token);
    /*通过令牌换取用户信息*/
    UserInfo selectByToken(String token);
}