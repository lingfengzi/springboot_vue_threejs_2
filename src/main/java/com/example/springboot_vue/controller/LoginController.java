package com.example.springboot_vue.controller;

import com.example.springboot_vue.model.UserInfo;
import com.example.springboot_vue.service.UserInfoService;
import com.example.springboot_vue.utils.JwtUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class LoginController {
    @Autowired
    UserInfoService userInfoService;

    @RequestMapping("/loginCheck")
    public @ResponseBody JSONObject loginCheck(@RequestBody JSONObject data, HttpServletResponse response){
        JSONObject json=new JSONObject();
        String username=data.getString("username");
        String password=data.getString("password");
        UserInfo userInfo= userInfoService.loginCheck(username,password);
        if(userInfo!= null){
            json.put("code",0);
            json.put("data",userInfo);
            json.put("msg","");

            String token= JwtUtil.createJWT(userInfo.getId().toString(),password);

            //更新存储令牌，一般存于redis中，示例存储在mysql
            userInfoService.updateUserToken(username,token);

            //从http头部设置token
            response.setHeader("authorization",token);
            // axios请求，跨域的情况下，这样设置才能从headers中拿到token
            response.setHeader("Access-Control-Expose-Headers", "authorization");
        }else{
            json.put("code",500);
            json.put("data",null);
            json.put("msg","用户名或密码错误");
        }
        return json;
    }

    @RequestMapping("/getUserInfo")
    public @ResponseBody JSONObject getUserInfo(HttpServletRequest request){
        String token=request.getHeader("authorization");
        JSONObject json=JwtUtil.parseJWT(token);

        long exp=Long.parseLong(json.getString("exp"));//过期时间
        long nbf=new Date().getTime();

        if(exp>nbf){
            UserInfo userInfo= userInfoService.selectByToken(token);
            if(userInfo !=null){
                json.put("code",0);
                json.put("data",userInfo);
                json.put("msg","");
            }else{
                json.put("code",500);
                json.put("data",null);
                json.put("msg","登录信息不存在，请重新登录");
            }
        }else{
            json.put("code",501);
            json.put("data",null);
            json.put("msg","登录信息已失效，请重新登录");
        }
        return json;
    }
}
