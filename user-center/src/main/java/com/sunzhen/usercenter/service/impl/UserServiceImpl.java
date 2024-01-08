package com.sunzhen.usercenter.service.impl;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunzhen.usercenter.model.domain.User;
import com.sunzhen.usercenter.service.UserService;
import com.sunzhen.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.sunzhen.usercenter.constant.UseConstant.USER_LOGIN_STATE;


/**
 * @author 86178
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-01-06 10:06:03
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {




    @Override
    public long userRegister(String userAccount, String userPassword, String checkWord) {
        User user = new User();
        user.setUserAccount(userAccount);
//        1、校验前端发送的数据
        String accoutPattern = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        String passwordPattern = "^[a-zA-Z]\\w{5,17}$";

        Matcher accountMatcher = Pattern.compile(accoutPattern).matcher(userAccount);
        Matcher passwordMatcher = Pattern.compile(passwordPattern).matcher(userPassword);

        if (!StringUtils.isAnyBlank(userAccount, userPassword, checkWord)) {
            if (accountMatcher.find() && passwordMatcher.find()) {
                if (userPassword.equals(checkWord)) {
                    User result = this.baseMapper
                            .selectOne(new QueryWrapper<User>().eq("user_account", userAccount));
                    if (result == null) {
//                        密码加密
                        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                        String encode = passwordEncoder.encode(userPassword);
                        user.setUserPassword(encode);
                        boolean save = this.save(user);
                        if (save) {
                            return user.getId();
                        } else
                            return -1;
                    }
                }

            }
        }
        return -1;
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {

 // TODO 1、 设置统一返回异常类

//        1、校验前端发送的数据
        String accoutPattern = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        String passwordPattern = "^[a-zA-Z]\\w{5,17}$";

        Matcher accountMatcher = Pattern.compile(accoutPattern).matcher(userAccount);
        Matcher passwordMatcher = Pattern.compile(passwordPattern).matcher(userPassword);

        if (!StringUtils.isAnyBlank(userAccount, userPassword)) {
            if (accountMatcher.find() && passwordMatcher.find()) {
                User user = this.baseMapper
                        .selectOne(
                                new QueryWrapper<User>().eq("user_account", userAccount));
//                密码校验
                if (user != null) {
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    if (passwordEncoder.matches(userPassword, user.getUserPassword())) {
//                        用户信息脱敏
                        User backUser = new User();
                        backUser.setId(user.getId());
                        backUser.setUsername(user.getUsername());
                        backUser.setPhone(user.getPhone());
                        backUser.setEmail(user.getEmail());
                        backUser.setUserAccount(user.getUserAccount());
                        backUser.setAvatar(user.getAvatar());
                        backUser.setSex(user.getSex());
                        backUser.setUserStatus(user.getUserStatus());
                        backUser.setCreateTime(user.getCreateTime());
                        backUser.setRole(user.getRole());

//                        记录用户信息
                        request.getSession().setAttribute(USER_LOGIN_STATE,backUser);

                        return backUser;
                    }
                    log.info("密码错误，请重试");
                }
                log.info("用户名错误，请重试");
            }
        }
        return null;
    }

    @Override
    public List<User> getUserByName(String userName) {
        if (userName == null) {
            List<User> userList = this.baseMapper.selectList(new QueryWrapper<>());
            return userList;
        }
        List<User> userList = this.baseMapper.selectList(new QueryWrapper<User>()
                .like("username", userName)
                .or()
                .like("user_account", userName));
        return userList;
    }
}




