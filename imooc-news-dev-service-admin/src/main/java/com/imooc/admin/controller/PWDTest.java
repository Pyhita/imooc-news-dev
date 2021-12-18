package com.imooc.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PWDTest {
    public static void main(String[] args) {
        // 使用spring自带的库进行加密
        String pwd = BCrypt.hashpw("admin", BCrypt.gensalt());

        System.out.println(pwd);
    }

}
