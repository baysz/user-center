package com.sunzhen.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

// 用于在序列化的时候不会出现冲突
    @Serial
    private static final long serialVersionUID = 2025918288886576540L;

//    userAccount, userPassword, checkWord

    private String userAccount;
    private String userPassword;
    private String checkWord;
}
