package com.sunzhen.usercenter.service;

import com.sunzhen.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86178
* @description 针对表【user】的数据库操作Service
* @createDate 2024-01-06 10:06:03
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     * @param userAccount    用户账号
     * @param userPassword   登陆密码
     * @param checkWord      验证码
     * @return  用户id
     */
    long userRegister(String userAccount,String userPassword,String checkWord);

    /**
     * 用户登陆
     *
     * @param userAccount  用户账号
     * @param userPassword 登陆密码
     * @param request
     * @return 用户信息
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    List<User> getUserByName(String userName);
}
