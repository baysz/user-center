package com.sunzhen.usercenter.controller;

import com.sunzhen.usercenter.model.domain.User;
import com.sunzhen.usercenter.model.request.UserRegisterRequest;
import com.sunzhen.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.sunzhen.usercenter.constant.UseConstant.USER_LOGIN_STATE;
import static com.sunzhen.usercenter.constant.UseConstant.USER_ROLE_MANAGER;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public Long register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest != null) {
            String userAccount = userRegisterRequest.getUserAccount();
            String userPassword = userRegisterRequest.getUserPassword();
            String checkWord = userRegisterRequest.getCheckWord();
            if (StringUtils.isAnyBlank(userAccount, userPassword, checkWord)) {
                long id = userService.userRegister(userAccount, userPassword, checkWord);
                return id;
            }
        }
        return null;
    }

    /**
     * 登陆
     *
     * @param userRegisterRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public User login(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        if (userRegisterRequest != null) {
            String userAccount = userRegisterRequest.getUserAccount();
            String userPassword = userRegisterRequest.getUserPassword();
            if (!StringUtils.isAnyBlank(userAccount, userPassword)) {
                User user = userService.doLogin(userAccount, userPassword, request);
                return user;
            }
        }
        return null;
    }

    @GetMapping("/search")
    public List<User> searchUsers(String userName,HttpServletRequest request) {
//        仅管理员可操作
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) attribute;
        if (user == null || user.getRole() != USER_ROLE_MANAGER) {
            return new ArrayList<>();
        }
        List<User> userList = userService.getUserByName(userName);
        return userList;
    }

    @PostMapping("/delete")
    public Boolean deleteUser(@RequestBody long id,HttpServletRequest request) {
        //        仅管理员可操作
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) attribute;
        if (user == null || user.getRole() != USER_ROLE_MANAGER) {
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return  userService.removeById(id);
    }
}

