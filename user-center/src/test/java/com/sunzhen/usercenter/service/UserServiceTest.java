package com.sunzhen.usercenter.service;
import java.util.Date;

import com.sunzhen.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Test
    void addTest() {
        User user = new User();
        user.setUsername("你好");
        user.setPhone("123546");
        user.setEmail("456113fas");
        user.setUserAccount("2154897");
        user.setUserPassword("xxxxxx");
        user.setAvatar("");
        user.setSex(0);
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setDeleted(0);
        user.setRole(0);

        boolean result = userService.save(user);
        Assertions.assertTrue(result);

    }

    @Test
    void userRegister() {
        String userAccount = "Asdf45643";
        String userPassword = "a123456789";
        String checkWord = "a123456789";

        long result = userService.userRegister(userAccount, userPassword, checkWord);

        System.out.println(result);
    }
}